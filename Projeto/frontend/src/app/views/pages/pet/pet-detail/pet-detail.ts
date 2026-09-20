import { speciesIcon } from '../../../../shared/pet-species-icon';
import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Vaccine } from '../../../../models/domain/vaccine';
import { CreateVaccineDto } from '../../../../models/dto/create-vaccine-dto';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetUpdateService } from '../../../../services/pet/pet-update';
import { PetDeleteService } from '../../../../services/pet/pet-delete';
import { VaccineReadService } from '../../../../services/vaccine/vaccine-read';
import { VaccineCreateService } from '../../../../services/vaccine/vaccine-create';
import { VaccineDeleteService } from '../../../../services/vaccine/vaccine-delete';
import { VaccineUpdateService } from '../../../../services/vaccine/vaccine-update';
import { Pet, PetGender, PetSpecies } from '../../../../models/domain/pet';
import { AnimalMode } from '../../../../models/domain/animalMode';
import { AnimalModeService } from '../../../../services/animalMode/animalMode';
import { FARM_SPECIES_OPTIONS, PET_SPECIES_OPTIONS, SpeciesOption, genderLabel } from '../../../../shared/pet-options';
import { Observable } from 'rxjs';
import { UpdateFarmAnimalDto } from '../../../../models/dto/update-farm-animal-dto';
import { FarmAnimalReadService } from '../../../../services/farm-animal/farm-animal-read';
import { FarmAnimalUpdateService } from '../../../../services/farm-animal/farm-animal-update';
import { FarmAnimalDeleteService } from '../../../../services/farm-animal/farm-animal-delete';




@Component({
  selector: 'app-pet-detail',
  imports: [RouterLink, ReactiveFormsModule, DatePipe, MatIconModule],
  templateUrl: './pet-detail.html',
  styleUrl: './pet-detail.css',
})
export class PetDetail implements OnInit {

  readonly PetSpecies = PetSpecies;

  readonly PetGender = PetGender;

  get isFarm(): boolean {
    return this.animalModeService.get() === AnimalMode.FARM;
  }

  get speciesOptions(): SpeciesOption[] {
    return this.isFarm ? FARM_SPECIES_OPTIONS : PET_SPECIES_OPTIONS;
  }

  genderLabel(gender?: string): string {
    return genderLabel(gender);
  }

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

  private currentPetId: string | null = null;

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
    private animalModeService: AnimalModeService,
    private farmAnimalReadService: FarmAnimalReadService,
    private farmAnimalUpdateService: FarmAnimalUpdateService,
    private farmAnimalDeleteService: FarmAnimalDeleteService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone,
  ) {
    this.vaccineForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      applicationDate: ['', [Validators.required]],
      expirationDate: [''],
      batch: [''],
      description: [''],
    });

    this.vaccineEditForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      applicationDate: ['', [Validators.required]],
      expirationDate: [''],
      batch: [''],
      description: [''],
    });

    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      species: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
      identifier: [''],
      weight: [null, [Validators.min(0.1)]],
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (!id) {
        return;
      }
      this.currentPetId = id;
      this.loading = true;
      this.zone.run(() => this.cdr.detectChanges());
      void this.loadPetData(id);
    });
  }

  private async loadPetData(id: string): Promise<void> {
    try {
      let pet: Pet;
      let vaccines: Vaccine[] = [];

      if (this.isFarm) {
        pet = await this.farmAnimalReadService.findById(id);
      } else {
        [pet, vaccines] = await Promise.all([
          this.petReadService.findById(id),
          this.vaccineReadService.findByPetId(id),
        ]);
      }

      this.zone.run(() => {
        this.pet = pet;
        this.vaccines = vaccines;
        this.loading = false;
        this.cdr.detectChanges();
      });
    } catch (error) {
      console.error('Erro ao carregar dados do pet', error);
      this.zone.run(() => {
        this.pet = null;
        this.loading = false;
        this.cdr.detectChanges();
      });
    }
  }

  private reloadCurrentPet(): void {
    if (this.currentPetId) {
      void this.loadPetData(this.currentPetId);
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

    const updatedPet: Pet = { ...this.pet, forAdoption: !this.pet.forAdoption };
    this.pendingAdoptionConfirmation = false;

    this.petUpdateService.update(updatedPet).subscribe({
      next: () => this.reloadCurrentPet(),
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
        gender: this.pet.gender ?? '',
        breed: this.pet.breed,
        birthDate: this.pet.birthDate,
        identifier: this.pet.identify ?? '',
        weight: this.pet.weight ?? null,
      });
    }
  }

  validateEditFields(): boolean {
    if (!this.isFarm) {
      return this.editForm.valid;
    }
    const controls = this.editForm.controls;
    return controls['breed'].valid
      && controls['birthDate'].valid
      && Number(controls['weight'].value) > 0;
  }

  saveEdit(): void {
    this.editValidationFailed = false;
    this.editSavedOk = false;

    if (!this.pet?.id || !this.validateEditFields()) {
      this.editValidationFailed = true;
      return;
    }

    const petId = this.pet.id;
    const controls = this.editForm.controls;
    let request$: Observable<unknown>;

    if (this.isFarm) {
      const farmAnimal: UpdateFarmAnimalDto = {
        id: petId,
        name: controls['name'].value?.trim() || undefined,
        breed: controls['breed'].value,
        birthDate: controls['birthDate'].value,
        weight: Number(controls['weight'].value),
        forSell: this.pet.forSell ?? false,
      };
      request$ = this.farmAnimalUpdateService.update(farmAnimal);
    } else {
      const updatedPet: Pet = {
        ...this.pet,
        name: controls['name'].value?.trim() || undefined,
        species: controls['species'].value,
        breed: controls['breed'].value,
        gender: controls['gender'].value,
        birthDate: controls['birthDate'].value,
      };
      request$ = this.petUpdateService.update(updatedPet);
    }

    request$.subscribe({
      next: async () => {
        await this.loadPetData(petId.toString());
        this.zone.run(() => {
          this.editSavedOk = true;
          this.showEditForm = false;
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao atualizar', error);
        this.zone.run(() => {
          this.editValidationFailed = true;
          this.cdr.detectChanges();
        });
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

    const request$: Observable<unknown> = this.isFarm
      ? this.farmAnimalDeleteService.delete(this.pet.id)
      : this.petDeleteService.delete(this.pet.id);

    request$.subscribe({
      next: () => this.zone.run(() => this.router.navigate(['/painel/pets'])),
      error: (error) => console.error('Erro ao excluir', error),
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

    const createVaccineDto: CreateVaccineDto = {
      idPet: this.pet.id!,
      name: this.vaccineForm.controls['name'].value,
      applicationDate: this.vaccineForm.controls['applicationDate'].value,
      expirationDate: this.vaccineForm.controls['expirationDate'].value || undefined,
      lote: this.vaccineForm.controls['batch'].value || undefined,
      description: this.vaccineForm.controls['description'].value || undefined,
    };

    this.vaccineCreateService.create(createVaccineDto).subscribe({
      next: () => {
        this.reloadCurrentPet();
        this.zone.run(() => {
          this.vaccineCreatedOk = true;
          this.vaccineForm.reset();
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao cadastrar vacina', error);
        this.zone.run(() => {
          this.vaccineCreateValidationFailed = true;
          this.cdr.detectChanges();
        });
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
        this.reloadCurrentPet();
        this.zone.run(() => {
          this.pendingVaccineDeleteId = null;
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao excluir vacina', error);
        this.zone.run(() => {
          this.pendingVaccineDeleteId = null;
          this.cdr.detectChanges();
        });
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
      expirationDate: vaccine.expirationDate || '',
      batch: vaccine.batch || '',
      description: vaccine.description || '',
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
      expirationDate: this.vaccineEditForm.controls['expirationDate'].value || undefined,
      batch: this.vaccineEditForm.controls['batch'].value || undefined,
      description: this.vaccineEditForm.controls['description'].value || undefined,
    };

    this.vaccineUpdateService.update(updatedVaccine).subscribe({
      next: () => {
        this.reloadCurrentPet();
        this.zone.run(() => {
          this.editingVaccineId = null;
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao atualizar vacina', error);
        this.zone.run(() => {
          this.vaccineEditValidationFailed = true;
          this.cdr.detectChanges();
        });
      },
    });
  }
}