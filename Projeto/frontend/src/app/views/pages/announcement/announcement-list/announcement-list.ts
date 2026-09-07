import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { AccountType } from '../../../../models/domain/user';
import { Announcement, AnnouncementType } from '../../../../models/domain/announcement';
import { AnnouncementReadService } from '../../../../services/announcement/announcement-read';
import { AnnouncementCreateService } from '../../../../services/announcement/announcement-create';
import { AnnouncementDeleteService } from '../../../../services/announcement/announcement-delete';
import { CurrentUserService } from '../../../../services/security/current-user';

import { AnnouncementCardComponent } from '../announcement-card';

@Component({
  selector: 'app-announcement-list',
  imports: [ReactiveFormsModule, DatePipe, AnnouncementCardComponent],
  templateUrl: './announcement-list.html',
  styleUrl: './announcement-list.css',
})
export class AnnouncementList implements OnInit {

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
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      title: ['', [Validators.required]],
      description: ['', [Validators.required]],
      date: [''],
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      let user = this.currentUserService.get();
      if (!user) {
        user = await this.currentUserService.load();
      }

      this.isCompany = user?.accountType === AccountType.EMPRESA;
      this.userEmail = user?.email ?? '';

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

    if (!this.isCompany || !this.validateFields()) {
      this.createValidationFailed = true;
      return;
    }

    const user = this.currentUserService.get();

    const announcement: Announcement = {
      title: this.form.controls['title'].value,
      description: this.form.controls['description'].value,
      date: this.form.controls['date'].value || undefined,
      creatorEmail: user?.email ?? this.userEmail,
      creatorName: user?.fullname ?? 'Empresa parceira',
      type: AnnouncementType.VACCINE
    };

    this.announcementCreateService.create(announcement).subscribe({
      next: (created) => {
        this.announcements = [created, ...this.announcements];
        this.form.reset();
        this.showForm = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao publicar campanha', error);
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
        this.cdr.detectChanges();
      },
      error: (error) => console.error('Erro ao remover campanha', error),
    });
  }
}
