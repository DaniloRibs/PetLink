import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { User, AccountType } from '../../../models/domain/user';
import { UserRole } from '../../../models/domain/user-role';
import { UserCreateService } from '../../../services/user/user-create';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css',
})
export class SignUp {

  readonly AccountType = AccountType;

  fullnameMiniLength: number = 2;
  fullnameMaxLength: number = 60;

  form: FormGroup;

  createValidationFailed: boolean = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private createService: UserCreateService,
  ) {
    this.form = this.formBuilder.group({
      accountType: [AccountType.PESSOA, [Validators.required]],
      fullname: ['', [
        Validators.required,
        Validators.minLength(this.fullnameMiniLength),
        Validators.maxLength(this.fullnameMaxLength),
      ]],
      email: ['', [
        Validators.required,
        Validators.email,
      ]],
      phone: ['', [
        Validators.required,
      ]],
      document: ['', [
        Validators.required,
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(4),
      ]],
      repeatPassword: ['', [
        Validators.required,
      ]],
    });
  }

  get isEmpresa() {
    return this.form.controls['accountType'].value === AccountType.EMPRESA;
  }

  passwordsMatch() {
    return this.form.controls['password'].value === this.form.controls['repeatPassword'].value;
  }

  validateFields() {
    return this.form.valid && this.passwordsMatch();
  }

  createAccount() {
    this.createValidationFailed = false;
    this.form.markAllAsTouched();

    if (!this.validateFields()) {
      return;
    }

    let user: User = {
      fullname: this.form.controls['fullname'].value,
      email: this.form.controls['email'].value,
      password: this.form.controls['password'].value,
      role: UserRole.USER,
      phone: this.form.controls['phone'].value,
      accountType: this.form.controls['accountType'].value,
      document: this.form.controls['document'].value,
    };

    this.createService.create(user).subscribe({
      next: () => {
        this.router.navigate(['/account/sign-in']);
      },
      error: (error) => {
        console.error(error);
        this.createValidationFailed = true;
      },
    });
  }
}
