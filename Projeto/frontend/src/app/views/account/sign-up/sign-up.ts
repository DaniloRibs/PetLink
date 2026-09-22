import { Component, OnDestroy, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
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
export class SignUp implements OnInit, OnDestroy {

  readonly AccountType = AccountType;

  fullnameMiniLength: number = 2;
  fullnameMaxLength: number = 60;

  form: FormGroup;

  createValidationFailed: boolean = false;

  readonly facts: string[] = [
    'Cães conseguem aprender e reconhecer centenas de palavras e comandos.',
    'Gatos usam os bigodes para perceber o espaço ao seu redor.',
    'Cavalos têm uma excelente memória e conseguem reconhecer pessoas conhecidas.',
    'Vacas passam várias horas por dia mastigando e descansando.',
    'Porcos usam o focinho para explorar o ambiente e encontrar alimentos.',
    'Ovelhas conseguem reconhecer e diferenciar os rostos de outros animais.',
    'Galinhas conseguem se comunicar usando diferentes sons para situações diferentes.',
    'Papagaios podem aprender a imitar sons e até palavras humanas.',
    'Cães possuem um olfato muito mais desenvolvido que o dos seres humanos.',
    'Gatos conseguem girar as orelhas para localizar melhor de onde vem um som.',
    'Cavalos conseguem correr poucas horas depois de nascer.',
    'Vacas produzem leite após o nascimento de um bezerro.',
    'Porcos gostam de explorar novos ambientes usando principalmente o focinho.',
    'Ovelhas possuem lã que ajuda a protegê-las contra o frio.',
    'Galinhas podem formar uma hierarquia social dentro do grupo.',
    'Algumas aves domésticas conseguem reconhecer a voz de seus donos.',
    'Cães usam diferentes posições de orelhas e cauda para demonstrar emoções.',
    'Gatos passam bastante tempo se limpando para manter o pelo em boas condições.',
    'Cavalos possuem olhos grandes que ajudam a ampliar seu campo de visão.',
    'Vacas conseguem reconhecer outras vacas do mesmo rebanho.',
    'Porcos são animais sociais e costumam viver bem em grupos.',
    'Ovelhas vivem em grupos e utilizam diferentes sons para se comunicar.',
    'Galinhas podem criar fortes vínculos com outras aves do mesmo grupo.',
    'Papagaios e outras aves possuem bicos adaptados para diferentes tipos de alimentação.'
  ];

  currentFact: number = 0;

  private readonly platformId = inject(PLATFORM_ID);
  private factTimer: ReturnType<typeof setInterval> | null = null;

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

  ngOnInit(): void {
    this.startFactRotation();
  }

  ngOnDestroy(): void {
    if (this.factTimer !== null) {
      clearInterval(this.factTimer);
    }
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

  private startFactRotation(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }

    this.factTimer = setInterval(() => {
      this.currentFact = (this.currentFact + 1) % this.facts.length;
    }, 9000);
  }
}