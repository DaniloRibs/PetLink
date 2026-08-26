// import { Component, OnInit } from '@angular/core';

// import { Router } from '@angular/router';

// import { MatToolbarModule } from '@angular/material/toolbar';
// import { MatButtonModule } from '@angular/material/button';
// import { MatSidenavModule } from '@angular/material/sidenav';
// import { MatMenuModule } from '@angular/material/menu';
// import { MatIconModule } from '@angular/material/icon';
// import { MatListModule } from '@angular/material/list';
// import { MatExpansionModule } from '@angular/material/expansion';
// import { MatTooltipModule } from '@angular/material/tooltip';
// import { MatInputModule } from '@angular/material/input';

// import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

// import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

// import * as fontawesome from '@fortawesome/free-solid-svg-icons';
// import { User } from '../../../models/domain/user';
// import { UserRole } from '../../../models/domain/user-role';
// import { UserCreateService } from '../../../services/user/user-create';
// import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
// import { AuthenticationService } from '../../../services/security/authentication';

// @Component({
//   selector: 'app-sign-in',
//   standalone: true,
//   imports: [
//     MatToolbarModule,
//     FormsModule,
//     MatButtonModule,
//     MatSidenavModule,
//     MatMenuModule,
//     MatIconModule,
//     MatListModule,
//     MatExpansionModule,
//     MatTooltipModule,
//     FontAwesomeModule,
//     ReactiveFormsModule,
//     MatInputModule,
//   ],
//   templateUrl: './sign-in.html',
//   styleUrl: './sign-in.css',
// })
// export class SignIn implements OnInit {


//   passwordMiniLength: number = 1;
//   passwordMaxLength: number = 10;


//   email = new FormControl(null, [
//     Validators.email,
//   ]);
//   password = new FormControl(null, [
//     Validators.minLength(this.passwordMiniLength),
//     Validators.maxLength(this.passwordMaxLength)
//   ]);


//   loginValidationFailed: boolean = false;

//   constructor(private router: Router,
//     private authenticationService: AuthenticationService,

//   ) { }

//   ngOnInit(): void {
//     console.log('iniciando o sign-in');
//     this.loginValidationFailed = false;

//     this.loginIfCredentialsIsValid();
//   }

//   loginIfCredentialsIsValid() {
//     if (this.authenticationService.isAuthenticated()) {
//       this.router.navigate(['']);
//       return;
//     }
//   }


//   validateFields() {
//     return this.email.valid && this.password.valid;
//   }
//   login() {
//     let credentials: UserCredentialDto = {
//       email: this.email.value!,
//       password: this.password.value!,
//     };

//     console.log(credentials);

//     this.authenticationService.authenticate(credentials).subscribe({
//       next: (value: any) => {
//         console.log("login realizado");
//         console.log(value);

//         this.authenticationService.addDataToLocalStorage(value.email);

//         this.router.navigate(['']);
//       },
//       error: (error) => {
//         console.log("login deu errado");
//         console.log(error);

//       }


//     });

//   }



// }
// import { Component, OnInit } from '@angular/core';
// import { Router, RouterLink } from '@angular/router';
// import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

// import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
// import { AuthenticationService } from '../../../services/security/authentication';

// @Component({
//   selector: 'app-sign-in',
//   imports: [ReactiveFormsModule, RouterLink],
//   templateUrl: './sign-in.html',
//   styleUrl: './sign-in.css',
// })
// export class SignIn implements OnInit {

//   passwordMiniLength: number = 1;
//   passwordMaxLength: number = 10;

//   email = new FormControl('', [
//     Validators.required,
//     Validators.email,
//   ]);
//   password = new FormControl('', [
//     Validators.required,
//     Validators.minLength(this.passwordMiniLength),
//     Validators.maxLength(this.passwordMaxLength),
//   ]);

//   loginValidationFailed: boolean = false;

//   constructor(
//     private router: Router,
//     private authenticationService: AuthenticationService,
//   ) { }

//   ngOnInit(): void {
//     this.loginValidationFailed = false;
//     this.loginIfCredentialsIsValid();
//   }

//   loginIfCredentialsIsValid() {
//     if (this.authenticationService.isAuthenticated()) {
//       this.router.navigate(['/painel']);
//     }
//   }

//   validateFields() {
//     return this.email.valid && this.password.valid;
//   }

//   login() {
//     this.loginValidationFailed = false;

//     let credentials: UserCredentialDto = {
//       email: this.email.value!,
//       password: this.password.value!,
//     };

//     this.authenticationService.authenticate(credentials).subscribe({
//       next: (user) => {
//         this.authenticationService.addDataToLocalStorage(user.email);
//         this.router.navigate(['/painel']);
//       },
//       error: (error) => {
//         console.log('login deu errado', error);
//         this.loginValidationFailed = true;
//       },
//     });
//   }
// }

// import { Component, OnInit } from '@angular/core';
// import { Router, RouterLink } from '@angular/router';
// import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

// import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
// import { AuthenticationService } from '../../../services/security/authentication';

// @Component({
//   selector: 'app-sign-in',
//   imports: [ReactiveFormsModule, RouterLink],
//   templateUrl: './sign-in.html',
//   styleUrl: './sign-in.css',
// })
// export class SignIn implements OnInit {
//   passwordMiniLength: number = 1;
//   passwordMaxLength: number = 10;

//   email = new FormControl('', [
//     Validators.required,
//     Validators.email,
//   ]);

//   password = new FormControl('', [
//     Validators.required,
//     Validators.minLength(1),
//     Validators.maxLength(10),
//   ]);

//   loginValidationFailed: boolean = false;
//   errorMessage: string = '';

//   constructor(
//     private router: Router,
//     private authenticationService: AuthenticationService,
//   ) { }

//   ngOnInit(): void {
//     this.loginValidationFailed = false;
//     this.loginIfCredentialsIsValid();
//   }

//   loginIfCredentialsIsValid() {
//     if (this.authenticationService.isAuthenticated()) {
//       this.router.navigate(['/painel']);
//     }
//   }

//   validateFields(): boolean {
//     return this.email.valid && this.password.valid;
//   }

//   login() {
//     this.loginValidationFailed = false;

//     if (!this.validateFields()) {
//       return;
//     }

//     let credentials: UserCredentialDto = {
//       email: this.email.value!,
//       password: this.password.value!,
//     };

//     this.authenticationService.authenticate(credentials).subscribe({
//       next: (user) => {
//         this.authenticationService.addDataToLocalStorage(user.email);
//         this.router.navigate(['/painel']);
//       },
//       error: (err) => {
//         console.error('Erro ao realizar login:', err);
//         this.loginValidationFailed = true;
//         this.errorMessage = err.message || 'Email e/ou senha incorretos';
//       },
//     });
//   }
// }

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

  // Antes tinha um Validators.maxLength(10) na senha: qualquer senha com
  // mais de 10 caracteres desabilitava o botao "Entrar" sem avisar nada.
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

    // Antes: se validateFields() desse falso, o botao ficava desabilitado
    // e nada acontecia (sem nenhum aviso). Agora, se o form nao for
    // valido, mostramos os erros de cada campo em vez de so travar o botao.
    this.email.markAsTouched();
    this.password.markAsTouched();

    if (!this.validateFields()) {
      console.log('login() abortado: formulario invalido', {
        email: this.email.value,
        emailErrors: this.email.errors,
        passwordErrors: this.password.errors,
      });
      return;
    }

    let credentials: UserCredentialDto = {
      email: this.email.value!,
      password: this.password.value!,
    };

    console.log('login() chamando authenticate com', credentials);

    this.authenticationService.authenticate(credentials).subscribe({
      next: (user) => {
        console.log('login() sucesso, usuario:', user);
        this.authenticationService.addDataToLocalStorage(user.email);
        this.router.navigate(['/painel']);
      },
      error: (error) => {
        console.log('login deu errado', error);
        this.loginValidationFailed = true;
      },
    });
  }
}
