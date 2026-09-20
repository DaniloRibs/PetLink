import { speciesIcon } from '../../../../shared/pet-species-icon';
import { FARM_SPECIES_OPTIONS, PET_SPECIES_OPTIONS, SpeciesOption, filterByMode, genderLabel } from '../../../../shared/pet-options';
import { ChangeDetectorRef, NgZone, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet, PetGender } from '../../../../models/domain/pet';
import { AnimalMode } from '../../../../models/domain/animalMode';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetCreateService } from '../../../../services/pet/pet-create';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AnimalModeService } from '../../../../services/animalMode/animalMode';
import { Observable } from 'rxjs';
import { CreateFarmAnimalDto } from '../../../../models/dto/create-farm-animal-dto';
import { FarmAnimalCreateService } from '../../../../services/farm-animal/farm-animal-create';
import { FarmAnimalReadService } from '../../../../services/farm-animal/farm-animal-read';

@Component({
  selector: 'app-pet-list',
  imports: [ReactiveFormsModule, MatIconModule],
  templateUrl: './pet-list.html',
  styleUrl: './pet-list.css',
})
export class PetList implements OnInit {

  readonly PetGender = PetGender;

  allPets: Pet[] = [];
  allFarmAnimals: Pet[] = [];
  loading: boolean = true;

  showForm: boolean = false;
  form: FormGroup;
  createValidationFailed: boolean = false;

  get isFarm(): boolean {
    return this.animalModeService.get() === AnimalMode.FARM;
  }

  get speciesOptions(): SpeciesOption[] {
    return this.isFarm ? FARM_SPECIES_OPTIONS : PET_SPECIES_OPTIONS;
  }

  get pets(): Pet[] {
    return this.isFarm ? this.allFarmAnimals : filterByMode(this.allPets, false);
  }

  speciesIcon(species: string): string {
    return speciesIcon(species);
  }

  genderLabel(gender?: string): string {
    return genderLabel(gender);
  }

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private zone: NgZone,
    private petReadService: PetReadService,
    private petCreateService: PetCreateService,
    private currentUserService: CurrentUserService,
    private animalModeService: AnimalModeService,
    private farmAnimalCreateService: FarmAnimalCreateService,
    private farmAnimalReadService: FarmAnimalReadService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      species: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
      identifier: [''],
      weight: [null, [Validators.min(0.1)]],
    });
  }

  async ngOnInit(): Promise<void> {
    await this.loadPets();
  }

  private async loadPets(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }

      const [pets, farmAnimals] = await Promise.all([
        this.petReadService.findByOwnerId(currentUser.id),
        this.farmAnimalReadService.findByOwnerId(currentUser.id).catch(error => {
          console.error('Erro ao carregar animais de fazenda', error);
          return [] as Pet[];
        }),
      ]);

      this.zone.run(() => {
        this.allPets = pets;
        this.allFarmAnimals = farmAnimals;
        this.loading = false;
        this.cdr.detectChanges();
      });
    } catch (error) {
      console.error('Erro ao carregar seus pets', error);
      this.zone.run(() => {
        this.loading = false;
        this.cdr.detectChanges();
      });
    }
  }

  openPet(pet: Pet): void {
    this.router.navigate(['/painel/pets', pet.id]);
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    this.createValidationFailed = false;
  }

  validateFields(): boolean {
    const controls = this.form.controls;
    const speciesValid = this.speciesOptions.some(option => option.value === controls['species'].value);

    if (!this.isFarm) {
      return this.form.valid && speciesValid;
    }

    const identifier = (controls['identifier'].value ?? '').trim();
    const weight = Number(controls['weight'].value);
    return controls['species'].valid
      && speciesValid
      && controls['gender'].valid
      && controls['breed'].valid
      && controls['birthDate'].valid
      && identifier.length > 0
      && weight > 0;
  }

  async createPet(): Promise<void> {
    this.createValidationFailed = false;

    if (!this.validateFields()) {
      this.createValidationFailed = true;
      return;
    }

    const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
    if (!currentUser?.id) {
      this.createValidationFailed = true;
      return;
    }

    const controls = this.form.controls;
    let request$: Observable<unknown>;

    if (this.isFarm) {
      const farmAnimal: CreateFarmAnimalDto = {
        name: controls['name'].value?.trim() || undefined,
        identify: controls['identifier'].value.trim(),
        species: controls['species'].value,
        breed: controls['breed'].value,
        birthDate: controls['birthDate'].value,
        gender: controls['gender'].value,
        weight: Number(controls['weight'].value),
        ownerId: currentUser.id,
      };
      request$ = this.farmAnimalCreateService.create(farmAnimal);
    } else {
      const pet: Pet = {
        name: controls['name'].value,
        species: controls['species'].value,
        gender: controls['gender'].value,
        breed: controls['breed'].value,
        birthDate: controls['birthDate'].value,
        ownerId: currentUser.id,
      };
      request$ = this.petCreateService.create(pet);
    }

    request$.subscribe({
      next: async () => {
        await this.loadPets();
        this.zone.run(() => {
          this.form.reset();
          this.showForm = false;
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao cadastrar', error);
        this.zone.run(() => {
          this.createValidationFailed = true;
          this.cdr.detectChanges();
        });
      },
    });
  }
}