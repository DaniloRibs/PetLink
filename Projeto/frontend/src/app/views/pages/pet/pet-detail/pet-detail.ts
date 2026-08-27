import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet } from '../../../../models/domain/pet';
import { Vaccine } from '../../../../models/domain/vaccine';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetUpdateService } from '../../../../services/pet/pet-update';
import { VaccineReadService } from '../../../../services/vaccine/vaccine-read';
import { VaccineCreateService } from '../../../../services/vaccine/vaccine-create';

@Component({
  selector: 'app-pet-detail',
  imports: [RouterLink, ReactiveFormsModule, DatePipe],
  templateUrl: './pet-detail.html',
  styleUrl: './pet-detail.css',
})
export class PetDetail implements OnInit {

  pet: Pet | null = null;
  vaccines: Vaccine[] = [];
  loading: boolean = true;
  vaccineForm: FormGroup;
  showVaccineForm: boolean = false;
  vaccineCreateValidationFailed: boolean = false;
  vaccineCreatedOk: boolean = false;
  pendingAdoptionConfirmation: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private petReadService: PetReadService,
    private petUpdateService: PetUpdateService,
    private vaccineReadService: VaccineReadService,
    private vaccineCreateService: VaccineCreateService,
    private cdr: ChangeDetectorRef,
  ) {
    this.vaccineForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      applicationDate: ['', [Validators.required]],
      nextDoseDate: [''],
      notes: [''],
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
}
