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

@Component({
  selector: 'app-pet-list',
  imports: [ReactiveFormsModule, MatIconModule],
  templateUrl: './pet-list.html',
  styleUrl: './pet-list.css',
})
export class PetList implements OnInit {

  readonly PetGender = PetGender;

  allPets: Pet[] = [];
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
    return filterByMode(this.allPets, this.isFarm);
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
      const pets = await this.petReadService.findByOwnerId(currentUser.id);
      this.zone.run(() => {
        this.allPets = pets;
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
    const species = this.form.controls['species'].value;
    const baseValid = this.form.valid && this.speciesOptions.some(option => option.value === species);

    if (!this.isFarm) {
      return baseValid;
    }

    const identifier = (this.form.controls['identifier'].value ?? '').trim();
    const weight = Number(this.form.controls['weight'].value);
    return baseValid && identifier.length > 0 && weight > 0;
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

    const pet: Pet = {
      name: this.form.controls['name'].value,
      species: this.form.controls['species'].value,
      gender: this.form.controls['gender'].value,
      breed: this.form.controls['breed'].value,
      birthDate: this.form.controls['birthDate'].value,
      ownerId: currentUser.id,
    };

    if (this.isFarm) {
      pet.identifier = this.form.controls['identifier'].value.trim();
      pet.weight = Number(this.form.controls['weight'].value);
      pet.forSell = false;
    }

    this.petCreateService.create(pet).subscribe({
      next: async () => {
        await this.loadPets();
        this.zone.run(() => {
          this.form.reset();
          this.showForm = false;
          this.cdr.detectChanges();
        });
      },
      error: (error) => {
        console.error('Erro ao cadastrar pet', error);
        this.zone.run(() => {
          this.createValidationFailed = true;
          this.cdr.detectChanges();
        });
      },
    });
  }
}