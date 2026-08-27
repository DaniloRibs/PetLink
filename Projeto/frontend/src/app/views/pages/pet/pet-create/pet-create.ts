import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet, PetSpecies } from '../../../../models/domain/pet';
import { PetCreateService } from '../../../../services/pet/pet-create';
import { AuthenticationService } from '../../../../services/security/authentication';

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
    private authenticationService: AuthenticationService,
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

  createPet() {
    this.createValidationFailed = false;

    if (!this.validateFields()) {
      this.createValidationFailed = true;
      return;
    }

    let pet: Pet = {
      name: this.form.controls['name'].value,
      species: this.form.controls['species'].value,
      breed: this.form.controls['breed'].value,
      birthDate: this.form.controls['birthDate'].value,
      ownerEmail: this.authenticationService.getAuthenticatedUserEmail(),
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
