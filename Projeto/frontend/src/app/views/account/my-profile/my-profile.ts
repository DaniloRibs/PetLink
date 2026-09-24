import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { provideNgxMask, NgxMaskDirective } from 'ngx-mask';
import { ToastrService } from 'ngx-toastr';

import { User, AccountType } from '../../../models/domain/user';
import { UserUpdateService } from '../../../services/user/user-update';
import { UserPasswordUpdateService } from '../../../services/user/user-password-update';
import { UserDeleteService } from '../../../services/user/user-delete';
import { CurrentUserService } from '../../../services/security/current-user';
import { AuthenticationService } from '../../../services/security/authentication';
import { optionalCpfValidator, optionalPhoneValidator, requiredCnpjValidator } from '../../../shared/document-validators';
import { strictEmailValidator } from '../../../shared/email-validator';

@Component({
  selector: 'app-my-profile',
  imports: [ReactiveFormsModule, RouterLink, MatIconModule, NgxMaskDirective],
  providers: [provideNgxMask()],
  templateUrl: './my-profile.html',
  styleUrl: './my-profile.css',
})
export class MyProfile implements OnInit {

  readonly AccountType = AccountType;
  entity: User | null = null;
  loading: boolean = true;

  form: FormGroup;
  showEditForm: boolean = false;
  updateOk: boolean = false;
  updateFailed: boolean = false;
  emailChanged: boolean = false;

  passwordForm: FormGroup;
  showChangePasswordForm: boolean = false;
  changingPassword: boolean = false;
  passwordUpdateFailed: boolean = false;
  passwordErrorMessage: string = '';

  readonly DELETE_KEYWORD = 'DELETAR';
  deleteForm: FormGroup;
  showDeleteAccountModal: boolean = false;
  deletingAccount: boolean = false;
  deleteAccountFailed: boolean = false;
  deleteAccountErrorMessage: string = '';

  get userInitial(): string {
    const name = this.entity?.fullname || this.entity?.email || '?';
    return name.trim().charAt(0).toUpperCase();
  }

  get documentLocked(): boolean {
    return !!this.entity?.document;
  }

  get isEmpresa(): boolean {
    return this.entity?.accountType === AccountType.ENTERPRISE;
  }

  constructor(
    private formBuilder: FormBuilder,
    private updateService: UserUpdateService,
    private passwordUpdateService: UserPasswordUpdateService,
    private userDeleteService: UserDeleteService,
    private router: Router,
    private currentUserService: CurrentUserService,
    private authenticationService: AuthenticationService,
    private toastrService: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      fullname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email, strictEmailValidator()]],
      phone: ['', [optionalPhoneValidator()]],
      document: ['', [optionalCpfValidator()]],
    });

    this.passwordForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    });

    this.deleteForm = this.formBuilder.group({
      confirmation: ['', [Validators.required, this.deleteKeywordValidator]],
    });
  }

  private deleteKeywordValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = String(control.value ?? '').trim();
    return value === this.DELETE_KEYWORD ? null : { keywordMismatch: true };
  };

  async ngOnInit(): Promise<void> {
    try {
      let user = this.currentUserService.get();
      if (!user) {
        user = await this.currentUserService.load();
      }

      this.entity = user;
      this.form.controls['fullname'].setValue(user?.fullname ?? '');
      this.form.controls['email'].setValue(user?.email ?? '');
      this.form.controls['phone'].setValue(user?.phone ?? '');
      this.form.controls['document'].setValue(user?.document ?? '');
      this.applyDocumentValidator();
    } catch (error) {
      console.error('Erro ao carregar dados do perfil', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  private applyDocumentValidator(): void {
    const documentControl = this.form.controls['document'];
    documentControl.setValidators(this.isEmpresa ? [requiredCnpjValidator()] : [optionalCpfValidator()]);
    documentControl.updateValueAndValidity();
  }

  validateFields(): boolean {
    return this.form.valid;
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
    this.updateOk = false;
    this.updateFailed = false;
    this.emailChanged = false;

    if (this.showEditForm && this.entity) {
      this.form.patchValue({
        fullname: this.entity.fullname,
        email: this.entity.email,
        phone: this.entity.phone ?? '',
        document: this.entity.document ?? '',
      });
      this.applyDocumentValidator();

      const documentControl = this.form.controls['document'];
      if (this.documentLocked) {
        documentControl.disable();
      } else {
        documentControl.enable();
      }
    }
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
    const phone = this.form.controls['phone'].value ?? '';
    const document = this.form.controls['document'].value ?? '';
    const emailWasChanged = email !== this.entity.email;

    try {
      await this.updateService.update(this.entity.id, fullname, email, phone, document);
      this.entity.fullname = fullname;
      this.entity.email = email;
      this.entity.phone = phone;
      this.entity.document = document;

      if (emailWasChanged) {
        this.authenticationService.addDataToLocalStorage(email);
        this.emailChanged = true;
      }

      this.updateOk = true;
      this.showEditForm = false;
    } catch (error) {
      console.error('Erro ao atualizar perfil', error);
      this.updateFailed = true;
    } finally {
      this.cdr.detectChanges();
    }
  }

  toggleChangePasswordForm(): void {
    this.showChangePasswordForm = !this.showChangePasswordForm;
    this.passwordUpdateFailed = false;
    this.passwordErrorMessage = '';

    if (this.showChangePasswordForm) {
      this.passwordForm.reset();
    }
  }

  async changePassword(): Promise<void> {
    this.passwordUpdateFailed = false;
    this.passwordErrorMessage = '';

    if (this.passwordForm.invalid) {
      this.passwordUpdateFailed = true;
      this.passwordErrorMessage = 'Preencha todos os campos corretamente.';
      return;
    }

    const oldPassword = this.passwordForm.controls['oldPassword'].value;
    const newPassword = this.passwordForm.controls['newPassword'].value;
    const confirmPassword = this.passwordForm.controls['confirmPassword'].value;

    if (newPassword !== confirmPassword) {
      this.passwordUpdateFailed = true;
      this.passwordErrorMessage = 'A confirmação não corresponde à nova senha.';
      return;
    }

    if (!this.entity?.id) {
      this.passwordUpdateFailed = true;
      this.passwordErrorMessage = 'Não foi possível identificar o usuário.';
      return;
    }

    this.changingPassword = true;
    try {
      await this.passwordUpdateService.updatePassword(this.entity.id, oldPassword, newPassword);
      this.toastrService.success('Senha alterada com sucesso!');
      this.showChangePasswordForm = false;
    } catch (error) {
      console.error('Erro ao atualizar senha', error);
      this.passwordUpdateFailed = true;
      this.passwordErrorMessage = 'Não foi possível alterar a senha. Confira os dados e tente novamente.';
    } finally {
      this.changingPassword = false;
      this.cdr.detectChanges();
    }
  }

  openDeleteAccountModal(): void {
    this.deleteForm.reset({ confirmation: '' });
    this.deleteAccountFailed = false;
    this.deleteAccountErrorMessage = '';
    this.showDeleteAccountModal = true;
  }

  closeDeleteAccountModal(): void {
    if (this.deletingAccount) {
      return;
    }
    this.showDeleteAccountModal = false;
  }

  async deleteAccount(): Promise<void> {
    this.deleteAccountFailed = false;
    this.deleteAccountErrorMessage = '';

    if (this.deleteForm.invalid) {
      this.deleteAccountFailed = true;
      this.deleteAccountErrorMessage = `Digite ${this.DELETE_KEYWORD} para confirmar a exclusão.`;
      return;
    }

    if (!this.entity?.id) {
      this.deleteAccountFailed = true;
      this.deleteAccountErrorMessage = 'Não foi possível identificar o usuário.';
      return;
    }

    this.deletingAccount = true;
    try {
      await this.userDeleteService.delete(this.entity.id);

      this.showDeleteAccountModal = false;
      this.authenticationService.logout();
      this.currentUserService.clear();
      this.toastrService.success('Sua conta foi excluída com sucesso.');
      await this.router.navigate(['/account/sign-in']);
    } catch (error) {
      console.error('Erro ao excluir conta', error);
      this.deleteAccountFailed = true;
      this.deleteAccountErrorMessage = 'Não foi possível excluir a conta. Tente novamente.';
    } finally {
      this.deletingAccount = false;
      this.cdr.detectChanges();
    }
  }
}