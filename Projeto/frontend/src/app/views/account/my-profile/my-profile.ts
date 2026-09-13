import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { User, AccountType } from '../../../models/domain/user';
import { UserUpdateService } from '../../../services/user/user-update';
import { CurrentUserService } from '../../../services/security/current-user';
import { AuthenticationService } from '../../../services/security/authentication';

@Component({
  selector: 'app-my-profile',
  imports: [ReactiveFormsModule, RouterLink, MatIconModule],
  templateUrl: './my-profile.html',
  styleUrl: './my-profile.css',
})
export class MyProfile implements OnInit {

  readonly AccountType = AccountType;
  entity: User | null = null;
  loading: boolean = true;

  form: FormGroup;
  updateOk: boolean = false;
  updateFailed: boolean = false;
  emailChanged: boolean = false;

  get userInitial(): string {
    const name = this.entity?.fullname || this.entity?.email || '?';
    return name.trim().charAt(0).toUpperCase();
  }

  constructor(
    private formBuilder: FormBuilder,
    private updateService: UserUpdateService,
    private currentUserService: CurrentUserService,
    private authenticationService: AuthenticationService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      fullname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      let user = this.currentUserService.get();
      if (!user) {
        user = await this.currentUserService.load();
      }

      this.entity = user;
      this.form.controls['fullname'].setValue(user?.fullname ?? '');
      this.form.controls['email'].setValue(user?.email ?? '');
    } catch (error) {
      console.error('Erro ao carregar dados do perfil', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  validateFields(): boolean {
    return this.form.valid;
  }

  async updateProfile(): Promise<void> {
    this.updateOk = false;
    this.updateFailed = false;
    this.emailChanged = false;

    if (!this.entity?.id || !this.validateFields()) {
      this.updateFailed = true;
      return;
    }

    const fullname = this.form.controls['fullname'].value;
    const email = this.form.controls['email'].value;
    const emailWasChanged = email !== this.entity.email;

    try {
      await this.updateService.update(this.entity.id, fullname, email);
      this.entity.fullname = fullname;
      this.entity.email = email;

      if (emailWasChanged) {
        this.authenticationService.addDataToLocalStorage(email);
        this.emailChanged = true;
      }

      this.updateOk = true;
    } catch (error) {
      console.error('Erro ao atualizar perfil', error);
      this.updateFailed = true;
    } finally {
      this.cdr.detectChanges();
    }
  }
}