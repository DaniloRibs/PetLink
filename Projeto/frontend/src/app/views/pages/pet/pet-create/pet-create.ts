import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet, PetSpecies } from '../../../../models/domain/pet';
import { PetCreateService } from '../../../../services/pet/pet-create';
import { CurrentUserService } from '../../../../services/security/current-user';

@Component({
  selector: 'app-pet-create',
  imports: [ReactiveFormsModule],
  templateUrl: './pet-create.html',
  styleUrl: './pet-create.css',
})
export class PetCreate {

  readonly PetSpecies = PetSpecies;

  form: FormGroup;
  createValidationFailed: boolean = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private petCreateService: PetCreateService,
    private currentUserService: CurrentUserService,
  ) {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      species: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
    });
  }

  validateFields() {
    return this.form.valid;
  }

  async createPet() {
    this.createValidationFailed = false;

    if (!this.validateFields()) {
      this.createValidationFailed = true;
      return;
    }

    const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
    if (!currentUser?.id) {
      console.error('Usuário atual não encontrado');
      this.createValidationFailed = true;
      return;
    }

    const ownerId: number = currentUser.id;

    let pet: Pet = {
      name: this.form.controls['name'].value,
      species: this.form.controls['species'].value,
      breed: this.form.controls['breed'].value,
      birthDate: this.form.controls['birthDate'].value,
      ownerId: ownerId,
    };

    this.petCreateService.create(pet).subscribe({
      next: () => {
        this.router.navigate(['/painel']);
      },
      error: (error) => {
        console.error(error);
        this.createValidationFailed = true;
      },
    });
  }
}
