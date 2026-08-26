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

// import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';

// import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

// import * as fontawesome from '@fortawesome/free-solid-svg-icons';
// import { User } from '../../../models/domain/user';
// import { UserRole } from '../../../models/domain/user-role';
// import { UserCreateService } from '../../../services/user/user-create';

// @Component({
//   selector: 'app-sign-up',
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
//   templateUrl: './sign-up.html',
//   styleUrl: './sign-up.css',
// })
// export class SignUp implements OnInit {

//   form: FormGroup;
//   fullnameMiniLength : number = 2;
//   fullnameMaxLength : number = 14;

//   passwordMiniLength : number = 1;
//   passwordMaxLength : number = 10;


//   constructor(
//     private router: Router,
//     private formBuilder: FormBuilder,
//     private createService: UserCreateService,

//   ) {
//     this.inicializeForm();
//   }



//   ngOnInit(): void {

//   }

//   inicializeForm(){

//     console.log('Formulario do sinup sendo inicializado');

//     this.form = this.formBuilder.group({
//       fullname: ['',[
//         Validators.required,
//         Validators.minLength(this.fullnameMiniLength),
//         Validators.maxLength(this.fullnameMaxLength),
//       ]],
//       email: ['',[
//         Validators.required,
//         Validators.email,
//       ]],
//       password: ['',[
//         Validators.required,
//         Validators.minLength(this.passwordMiniLength),
//         Validators.maxLength(this.passwordMaxLength),
//       ]],
//       repeatPassword: ['',[
//         Validators.required,
//         Validators.minLength(this.passwordMiniLength),
//         Validators.maxLength(this.passwordMaxLength),
//       ]]

//     });

//   }

//   createAccount(){
//     let user: User = {
//       fullname: this.form.controls['fullname'].value,
//       email : this.form.controls['email'].value,
//       password : this.form.controls['password'].value,
//       role: UserRole.USER,
//     }


//     console.log(user)
//     this.createService.create(user).subscribe({
//       next: value =>{
//         console.log('deu certo');
//         console.log(value);  
//         this.router.navigate(['/account/sign-in']);
//       },error: error => {
//         console.error(error);
//       }


//     });


//   }

//   validateFields(){
//     let isfullnameValid = this.form.controls['fullname'].valid;
//     let isEmailValid = this.form.controls['email'].valid;
//     let isPasswordValid = this.form.controls['password'].valid;
//     let isRepeatPasswordValid = this.form.controls['repeatPassword'].valid;
    

//     let password = this.form.controls['password'].value;
//     let repeatPassword = this.form.controls['repeatPassword'].value;

//     if(password != repeatPassword){
//       return false;
//     }
    
//     return isfullnameValid
//     && isEmailValid
//     && isPasswordValid
//     && isRepeatPasswordValid;

//   }


// }

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

  passwordMiniLength: number = 1;
  passwordMaxLength: number = 10;

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
        Validators.minLength(this.passwordMiniLength),
        Validators.maxLength(this.passwordMaxLength),
      ]],
      repeatPassword: ['', [
        Validators.required,
      ]],
    });
  }

  get isEmpresa() {
    return this.form.controls['accountType'].value === AccountType.EMPRESA;
  }

  validateFields() {
    const passwordsMatch = this.form.controls['password'].value === this.form.controls['repeatPassword'].value;
    return this.form.valid && passwordsMatch;
  }

  createAccount() {
    this.createValidationFailed = false;

    if (!this.validateFields()) {
      this.createValidationFailed = true;
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
