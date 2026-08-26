import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';

import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import * as fontawesome from '@fortawesome/free-solid-svg-icons';
import { User } from '../../../models/domain/user';
import { UserRole } from '../../../models/domain/user-role';
import { UserCreateService } from '../../../services/user/user-create';
import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
import { AuthenticationService } from '../../../services/security/authentication';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [
    MatToolbarModule,
    FormsModule,
    MatButtonModule,
    MatSidenavModule,
    MatMenuModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    MatTooltipModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    MatInputModule,
  ],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.css',
})
export class SignIn implements OnInit {


  passwordMiniLength: number = 1;
  passwordMaxLength: number = 10;


  email = new FormControl(null, [
    Validators.email,
  ]);
  password = new FormControl(null, [
    Validators.minLength(this.passwordMiniLength),
    Validators.maxLength(this.passwordMaxLength)
  ]);


  loginValidationFailed: boolean = false;

  constructor(private router: Router,
    private authenticationService: AuthenticationService,

  ) { }

  ngOnInit(): void {
    console.log('iniciando o sign-in');
    this.loginValidationFailed = false;

    this.loginIfCredentialsIsValid();
  }

  loginIfCredentialsIsValid() {
    if (this.authenticationService.isAuthenticated()) {
      this.router.navigate(['']);
      return;
    }
  }


  validateFields() {
    return this.email.valid && this.password.valid;
  }
  login() {
    let credentials: UserCredentialDto = {
      email: this.email.value!,
      password: this.password.value!,
    };

    console.log(credentials);

    this.authenticationService.authenticate(credentials).subscribe({
      next: (value: any) => {
        console.log("login realizado");
        console.log(value);

        this.authenticationService.addDataToLocalStorage(value.email);

        this.router.navigate(['']);
      },
      error: (error) => {
        console.log("login deu errado");
        console.log(error);

      }


    });

  }



}