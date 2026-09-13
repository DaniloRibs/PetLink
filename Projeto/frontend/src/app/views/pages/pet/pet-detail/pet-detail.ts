import { speciesIcon } from '../../../../shared/pet-species-icon';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet, PetSpecies } from '../../../../models/domain/pet';
import { Vaccine } from '../../../../models/domain/vaccine';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetUpdateService } from '../../../../services/pet/pet-update';
import { PetDeleteService } from '../../../../services/pet/pet-delete';
import { VaccineReadService } from '../../../../services/vaccine/vaccine-read';
import { VaccineCreateService } from '../../../../services/vaccine/vaccine-create';
import { VaccineDeleteService } from '../../../../services/vaccine/vaccine-delete';
import { VaccineUpdateService } from '../../../../services/vaccine/vaccine-update';

@Component({
  selector: 'app-pet-detail',
  imports: [RouterLink, ReactiveFormsModule, DatePipe, MatIconModule],
  templateUrl: './pet-detail.html',
  styleUrl: './pet-detail.css',
})
export class PetDetail implements OnInit {

  readonly PetSpecies = PetSpecies;

  pet: Pet | null = null;
  vaccines: Vaccine[] = [];
  loading: boolean = true;
  vaccineForm: FormGroup;
  showVaccineForm: boolean = false;
  vaccineCreateValidationFailed: boolean = false;
  vaccineCreatedOk: boolean = false;
  pendingAdoptionConfirmation: boolean = false;

  vaccineEditForm: FormGroup;
  editingVaccineId: number | null = null;
  vaccineEditValidationFailed: boolean = false;

  editForm: FormGroup;
  showEditForm: boolean = false;
  editValidationFailed: boolean = false;
  editSavedOk: boolean = false;

  speciesIcon(species: string): string {
    return speciesIcon(species);
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private petReadService: PetReadService,
    private petUpdateService: PetUpdateService,
    private petDeleteService: PetDeleteService,
    private vaccineReadService: VaccineReadService,
    private vaccineCreateService: VaccineCreateService,
    private vaccineDeleteService: VaccineDeleteService,
    private vaccineUpdateService: VaccineUpdateService,
    private cdr: ChangeDetectorRef,
  ) {
    this.vaccineForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      applicationDate: ['', [Validators.required]],
      nextDoseDate: [''],
      lot: [''],
      notes: [''],
    });

    this.vaccineEditForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      applicationDate: ['', [Validators.required]],
      nextDoseDate: [''],
      lot: [''],
      notes: [''],
    });

    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      species: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
    });
  }

  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id')!;

    try {
      const [pet, vaccines] = await Promise.all([
        this.petReadService.findById(id),
        this.vaccineReadService.findByPetId(id),
      ]);
      this.pet = pet;
      this.vaccines = vaccines;
    } catch (error) {
      console.error('Erro ao carregar dados do pet', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  requestToggleAdoption(): void {
    this.pendingAdoptionConfirmation = true;
  }

  cancelToggleAdoption(): void {
    this.pendingAdoptionConfirmation = false;
  }

  confirmToggleAdoption(): void {
    if (!this.pet) {
      return;
    }

    this.pet.forAdoption = !this.pet.forAdoption;
    this.pendingAdoptionConfirmation = false;

    this.petUpdateService.update(this.pet).subscribe({
      error: (error) => console.error('Erro ao atualizar pet', error),
    });
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
    this.editValidationFailed = false;
    this.editSavedOk = false;

    if (this.showEditForm && this.pet) {
      this.editForm.patchValue({
        name: this.pet.name,
        species: this.pet.species,
        breed: this.pet.breed,
        birthDate: this.pet.birthDate,
      });
    }
  }

  validateEditFields(): boolean {
    return this.editForm.valid;
  }

  saveEdit(): void {
    this.editValidationFailed = false;
    this.editSavedOk = false;

    if (!this.pet || !this.validateEditFields()) {
      this.editValidationFailed = true;
      return;
    }

    const updatedPet: Pet = {
      ...this.pet,
      name: this.editForm.controls['name'].value,
      species: this.editForm.controls['species'].value,
      breed: this.editForm.controls['breed'].value,
      birthDate: this.editForm.controls['birthDate'].value,
    };

    this.petUpdateService.update(updatedPet).subscribe({
      next: (updated) => {
        this.pet = updated;
        this.editSavedOk = true;
        this.showEditForm = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao atualizar pet', error);
        this.editValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  pendingDeleteConfirmation: boolean = false;

  requestDelete(): void {
    this.pendingDeleteConfirmation = true;
  }

  cancelDelete(): void {
    this.pendingDeleteConfirmation = false;
  }

  confirmDelete(): void {
    if (!this.pet?.id) {
      return;
    }

    this.petDeleteService.delete(this.pet.id).subscribe({
      next: () => this.router.navigate(['/painel/pets']),
      error: (error) => console.error('Erro ao excluir pet', error),
    });
  }

  toggleVaccineForm(): void {
    this.showVaccineForm = !this.showVaccineForm;
    this.vaccineCreateValidationFailed = false;
    this.vaccineCreatedOk = false;
  }

  validateVaccineFields(): boolean {
    return this.vaccineForm.valid;
  }

  createVaccine(): void {
    this.vaccineCreateValidationFailed = false;
    this.vaccineCreatedOk = false;

    if (!this.pet || !this.validateVaccineFields()) {
      this.vaccineCreateValidationFailed = true;
      return;
    }

    const vaccine: Vaccine = {
      petId: this.pet.id!,
      name: this.vaccineForm.controls['name'].value,
      applicationDate: this.vaccineForm.controls['applicationDate'].value,
      nextDoseDate: this.vaccineForm.controls['nextDoseDate'].value || undefined,
      lot: this.vaccineForm.controls['lot'].value || undefined,
      notes: this.vaccineForm.controls['notes'].value || undefined,
    };

    this.vaccineCreateService.create(vaccine).subscribe({
      next: (created) => {
        this.vaccines = [...this.vaccines, created];
        this.vaccineCreatedOk = true;
        this.vaccineForm.reset();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao cadastrar vacina', error);
        this.vaccineCreateValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  pendingVaccineDeleteId: number | null = null;

  requestDeleteVaccine(id: number): void {
    this.pendingVaccineDeleteId = id;
  }

  cancelDeleteVaccine(): void {
    this.pendingVaccineDeleteId = null;
  }

  confirmDeleteVaccine(id: number): void {
    this.vaccineDeleteService.delete(id).subscribe({
      next: () => {
        this.vaccines = this.vaccines.filter((vaccine) => vaccine.id !== id);
        this.pendingVaccineDeleteId = null;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao excluir vacina', error);
        this.pendingVaccineDeleteId = null;
        this.cdr.detectChanges();
      },
    });
  }

  startEditVaccine(vaccine: Vaccine): void {
    this.pendingVaccineDeleteId = null;
    this.editingVaccineId = vaccine.id ?? null;
    this.vaccineEditValidationFailed = false;

    this.vaccineEditForm.patchValue({
      name: vaccine.name,
      applicationDate: vaccine.applicationDate,
      nextDoseDate: vaccine.nextDoseDate || '',
      lot: vaccine.lot || '',
      notes: vaccine.notes || '',
    });
  }

  cancelEditVaccine(): void {
    this.editingVaccineId = null;
  }

  validateVaccineEditFields(): boolean {
    return this.vaccineEditForm.valid;
  }

  saveVaccineEdit(vaccine: Vaccine): void {
    this.vaccineEditValidationFailed = false;

    if (!this.validateVaccineEditFields()) {
      this.vaccineEditValidationFailed = true;
      return;
    }

    const updatedVaccine: Vaccine = {
      ...vaccine,
      name: this.vaccineEditForm.controls['name'].value,
      applicationDate: this.vaccineEditForm.controls['applicationDate'].value,
      nextDoseDate: this.vaccineEditForm.controls['nextDoseDate'].value || undefined,
      lot: this.vaccineEditForm.controls['lot'].value || undefined,
      notes: this.vaccineEditForm.controls['notes'].value || undefined,
    };

    this.vaccineUpdateService.update(updatedVaccine).subscribe({
      next: (saved) => {
        this.vaccines = this.vaccines.map((v) => (v.id === saved.id ? saved : v));
        this.editingVaccineId = null;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao atualizar vacina', error);
        this.vaccineEditValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }
}