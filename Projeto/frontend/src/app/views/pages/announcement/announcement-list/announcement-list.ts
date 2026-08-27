import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { AccountType } from '../../../../models/domain/user';
import { VaccineCampaign } from '../../../../models/domain/campaign';
import { VaccineCampaignReadService } from '../../../../services/campaign/campaign-read';
import { VaccineCampaignCreateService } from '../../../../services/campaign/campaign-create';
import { VaccineCampaignDeleteService } from '../../../../services/campaign/campaign-delete';
import { CurrentUserService } from '../../../../services/security/current-user';

@Component({
  selector: 'app-announcement-list',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './announcement-list.html',
  styleUrl: './announcement-list.css',
})
export class AnnouncementList implements OnInit {

  campaigns: VaccineCampaign[] = [];
  loading: boolean = true;
  isCompany: boolean = false;
  userEmail: string = '';

  showForm: boolean = false;
  form: FormGroup;
  createValidationFailed: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private campaignReadService: VaccineCampaignReadService,
    private campaignCreateService: VaccineCampaignCreateService,
    private campaignDeleteService: VaccineCampaignDeleteService,
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

      this.campaigns = await this.campaignReadService.findAll();
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

    const campaign: VaccineCampaign = {
      title: this.form.controls['title'].value,
      description: this.form.controls['description'].value,
      date: this.form.controls['date'].value || undefined,
      companyEmail: user?.email ?? this.userEmail,
      companyName: user?.fullname ?? 'Empresa parceira',
    };

    this.campaignCreateService.create(campaign).subscribe({
      next: (created) => {
        this.campaigns = [created, ...this.campaigns];
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

  removeCampaign(campaign: VaccineCampaign): void {
    if (!campaign.id) {
      return;
    }

    this.campaignDeleteService.delete(campaign.id).subscribe({
      next: () => {
        this.campaigns = this.campaigns.filter(c => c.id !== campaign.id);
        this.cdr.detectChanges();
      },
      error: (error) => console.error('Erro ao remover campanha', error),
    });
  }
}
