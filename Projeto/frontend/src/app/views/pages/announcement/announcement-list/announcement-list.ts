import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { AccountType, User } from '../../../../models/domain/user';
import { Announcement, AnnouncementType } from '../../../../models/domain/announcement';
import { CreateAnnouncementDto } from '../../../../models/dto/create-announcement-dto';
import { isValidPhone } from '../../../../shared/document-validators';
import { AnnouncementReadService } from '../../../../services/announcement/announcement-read';
import { AnnouncementCreateService } from '../../../../services/announcement/announcement-create';
import { AnnouncementDeleteService } from '../../../../services/announcement/announcement-delete';
import { CurrentUserService } from '../../../../services/security/current-user';

import { AnnouncementCardComponent } from '../announcement-card/announcement-card';
import { FormatToolbarComponent } from '../../../../shared/format-toolbar/format-toolbar';

@Component({
  selector: 'app-announcement-list',
  imports: [ReactiveFormsModule, AnnouncementCardComponent, MatIconModule, FormatToolbarComponent],
  templateUrl: './announcement-list.html',
  styleUrl: './announcement-list.css',
})
export class AnnouncementList implements OnInit {

  AnnouncementType = AnnouncementType;
  user: User | null = null;
  announcements: Announcement[] = [];
  loading: boolean = true;
  isCompany: boolean = false;
  userEmail: string = '';

  showForm: boolean = false;
  form: FormGroup;
  createValidationFailed: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private announcementReadService: AnnouncementReadService,
    private announcementCreateService: AnnouncementCreateService,
    private announcementDeleteService: AnnouncementDeleteService,
    private currentUserService: CurrentUserService,
    private toastrService: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      title: ['', [Validators.required]],
      type: ['', [Validators.required]],
      description: ['', [Validators.required]],
      date: [''],
      location: [''],
      contactMethod: ['email', [Validators.required]],
    });
  }

  get userHasValidPhone(): boolean {
    return isValidPhone(this.user?.phone ?? '');
  }

  get isLostSelected(): boolean {
    return this.form.controls['type'].value === AnnouncementType.LOST;
  }

  async ngOnInit(): Promise<void> {
    try {
      this.user = this.currentUserService.get();
      if (!this.user) {
        this.user = await this.currentUserService.load();
      }

      this.isCompany = this.user?.accountType === AccountType.ENTERPRISE;
      this.userEmail = this.user?.email ?? '';

      if (!this.isCompany) {
        this.form.patchValue({ type: AnnouncementType.LOST });
      }

      this.announcements = await this.announcementReadService.findAll();
    } catch (error) {
      console.error('Erro ao carregar campanhas de vacinação', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    this.createValidationFailed = false;
  }

  validateFields(): boolean {
    return this.form.valid;
  }

  createCampaign(): void {
    this.createValidationFailed = false;

    if (!this.validateFields() || !this.user?.id) {
      this.createValidationFailed = true;
      return;
    }

    const type = this.isCompany ? this.form.controls['type'].value : AnnouncementType.LOST;
    const isLost = type === AnnouncementType.LOST;

    const wantsPhone = this.form.controls['contactMethod'].value === 'phone';
    const contact = (wantsPhone && this.userHasValidPhone) ? this.user.phone! : this.user.email;

    const createAnnouncementDto: CreateAnnouncementDto = {
      title: this.form.controls['title'].value,
      description: this.form.controls['description'].value,
      eventDate: isLost ? undefined : (this.form.controls['date'].value || undefined),
      location: this.form.controls['location'].value || undefined,
      idCreator: this.user.id,
      announcementType: type,
      contact: contact,
    };

    this.announcementCreateService.create(createAnnouncementDto).subscribe({
      next: async () => {
        this.announcements = await this.announcementReadService.findAll();
        this.form.reset({ type: this.isCompany ? '' : AnnouncementType.LOST, contactMethod: 'email' });
        this.showForm = false;
        this.toastrService.success('Anúncio publicado com sucesso!');
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao publicar anúncio', error);
        this.createValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  removeCampaign(announcement: Announcement): void {
    if (!announcement.id) {
      return;
    }
    this.announcementDeleteService.delete(announcement.id).subscribe({
      next: () => {
        this.announcements = this.announcements.filter(c => c.id !== announcement.id);
        this.toastrService.success('Campanha removida.');
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao remover campanha', error);
        this.toastrService.error('Não foi possível remover a campanha.');
      },
    });
  }

  onAnnouncementUpdated(updated: Announcement): void {
    this.announcements = this.announcements.map(a => a.id === updated.id ? updated : a);
    this.cdr.detectChanges();
  }
}