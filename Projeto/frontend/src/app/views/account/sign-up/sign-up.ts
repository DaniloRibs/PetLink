import { Component } from '@angular/core';
import { provideNgxMask, NgxMaskDirective } from 'ngx-mask'
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AbstractControl, ValidationErrors } from '@angular/forms';


import { User, AccountType } from '../../../models/domain/user';
import { UserCreateService } from '../../../services/user/user-create';
import { optionalCpfValidator, optionalPhoneValidator, requiredCnpjValidator } from '../../../shared/document-validators';
import { strictEmailValidator } from '../../../shared/email-validator';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, RouterLink, NgxMaskDirective],
  providers: [provideNgxMask()],
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
      accountType: [AccountType.PERSON, [Validators.required]],
      fullname: ['', [
        Validators.required,
        Validators.minLength(this.fullnameMiniLength),
        Validators.maxLength(this.fullnameMaxLength),
      ]],
      email: ['', [
        Validators.required,
        Validators.email,
        strictEmailValidator(),
      ]],
      phone: ['', [
        optionalPhoneValidator(),
      ]],
      document: ['', [
        optionalCpfValidator(),
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(4),
      ]],
      repeatPassword: ['', [
        Validators.required,
      ]],
    });

    this.form.controls['accountType'].valueChanges.subscribe((accountType: AccountType) => {
      const documentControl = this.form.controls['document'];
      documentControl.setValidators(
        accountType === AccountType.ENTERPRISE ? [requiredCnpjValidator()] : [optionalCpfValidator()]
      );
      documentControl.updateValueAndValidity();
    });
  }

  get isEmpresa() {
    return this.form.controls['accountType'].value === AccountType.ENTERPRISE;
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