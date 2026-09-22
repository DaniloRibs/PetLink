import { Component, OnDestroy, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
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
export class SignIn implements OnInit, OnDestroy {

  email = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  password = new FormControl('', [
    Validators.required,
  ]);

  loginValidationFailed: boolean = false;

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
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.loginValidationFailed = false;
    this.loginIfCredentialsIsValid();
    this.startFactRotation();
  }

  ngOnDestroy(): void {
    if (this.factTimer !== null) {
      clearInterval(this.factTimer);
    }
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

  private startFactRotation(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }

    this.factTimer = setInterval(() => {
      this.currentFact = (this.currentFact + 1) % this.facts.length;
    }, 12000);
  }
}