import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { User } from '../../../models/domain/user';
import { UserUpdateService } from '../../../services/user/user-update';
import { CurrentUserService } from '../../../services/security/current-user';

@Component({
  selector: 'app-my-profile',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './my-profile.html',
  styleUrl: './my-profile.css',
})
export class MyProfile implements OnInit {

  entity: User | null = null;
  loading: boolean = true;

  form: FormGroup;
  updateOk: boolean = false;
  updateFailed: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private updateService: UserUpdateService,
    private currentUserService: CurrentUserService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      fullname: ['', [Validators.required, Validators.minLength(2)]],
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

    if (!this.entity?.id || !this.validateFields()) {
      this.updateFailed = true;
      return;
    }

    const fullname = this.form.controls['fullname'].value;

    try {
      await this.updateService.update(this.entity.id, fullname);
      this.entity.fullname = fullname;
      this.updateOk = true;
    } catch (error) {
      console.error('Erro ao atualizar perfil', error);
      this.updateFailed = true;
    } finally {
      this.cdr.detectChanges();
    }
  }
}
