import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet, PetSpecies } from '../../../../models/domain/pet';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetCreateService } from '../../../../services/pet/pet-create';
import { CurrentUserService } from '../../../../services/security/current-user';

@Component({
  selector: 'app-pet-list',
  imports: [ReactiveFormsModule],
  templateUrl: './pet-list.html',
  styleUrl: './pet-list.css',
})
export class PetList implements OnInit {

  readonly PetSpecies = PetSpecies;

  pets: Pet[] = [];
  loading: boolean = true;

  showForm: boolean = false;
  form: FormGroup;
  createValidationFailed: boolean = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private petReadService: PetReadService,
    private petCreateService: PetCreateService,
    private currentUserService: CurrentUserService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      species: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }
      this.pets = await this.petReadService.findByOwnerId(currentUser.id);
    } catch (error) {
      console.error('Erro ao carregar seus pets', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  openPet(pet: Pet): void {
    this.router.navigate(['/painel/pets', pet.id]);
  }

  speciesIcon(species: string): string {
    switch (species) {
      case 'cachorro': return '🐶';
      case 'gato': return '🐱';
      case 'passaro': return '🐦';
      default: return '🐾';
    }
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    this.createValidationFailed = false;
  }

  validateFields(): boolean {
    return this.form.valid;
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
      breed: this.form.controls['breed'].value,
      birthDate: this.form.controls['birthDate'].value,
      ownerId: currentUser.id,
    };

    this.petCreateService.create(pet).subscribe({
      next: (created) => {
        this.pets = [...this.pets, created];
        this.form.reset();
        this.showForm = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao cadastrar pet', error);
        this.createValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }
}
