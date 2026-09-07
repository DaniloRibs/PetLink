import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
import { AuthenticationService } from '../../../services/security/authentication';

@Component({
  selector: 'app-sign-in',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.css',
})
export class SignIn implements OnInit {

  email = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  password = new FormControl('', [
    Validators.required,
  ]);

  loginValidationFailed: boolean = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.loginValidationFailed = false;
    this.loginIfCredentialsIsValid();
  }

  loginIfCredentialsIsValid() {
    if (this.authenticationService.isAuthenticated()) {
      this.router.navigate(['/painel']);
    }
  }

  validateFields() {
    return this.email.valid && this.password.valid;
  }

  login() {
    this.loginValidationFailed = false;
    this.email.markAsTouched();
    this.password.markAsTouched();

    if (!this.validateFields()) {
      return;
    }

    let credentials: UserCredentialDto = {
      email: this.email.value!,
      password: this.password.value!,
    };

    this.authenticationService.authenticate(credentials).subscribe({
      next: (user) => {
        this.authenticationService.addDataToLocalStorage(user.email);
        this.router.navigate(['/painel']);
      },
      error: (error) => {
        console.error('Erro ao realizar login:', error);
        this.loginValidationFailed = true;
      },
    });
  }
}
