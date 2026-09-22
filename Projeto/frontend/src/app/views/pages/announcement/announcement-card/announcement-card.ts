import { CommonModule, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Announcement, AnnouncementType } from '../../../../models/domain/announcement';
import { AnnouncementUpdateService } from '../../../../services/announcement/announcement-update';
import { User } from '../../../../models/domain/user';
import { FormatTextPipe } from '../../../../shared/pipes/format-text/format-text.pipe';
import { FormatToolbarComponent } from '../../../../shared/format-toolbar/format-toolbar';

@Component({
  selector: 'announcement-card',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    FormatTextPipe,
    FormatToolbarComponent
  ],
  templateUrl: './announcement-card.html',
  styleUrl: './announcement-card.scss'
})
export class AnnouncementCardComponent {

  @Input({ required: true }) announcement!: Announcement;
  @Input({ required: true }) user!: User;

  @Input() canManage: boolean = false;
  @Output() delete = new EventEmitter<Announcement>();
  @Output() updated = new EventEmitter<Announcement>();

  AnnouncementType = AnnouncementType;
  showDetailModal = false;
  showEditForm: boolean = false;
  editForm: FormGroup;
  editValidationFailed: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private announcementUpdateService: AnnouncementUpdateService,
  ) {
    this.editForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      description: ['', [Validators.required]],
      date: [''],
      location: [''],
    });
  }

  onDelete(): void {
    this.delete.emit(this.announcement);
  }

  openDetails(): void {
    this.showDetailModal = true;
  }

  closeDetails(): void {
    this.showDetailModal = false;
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
    this.editValidationFailed = false;

    if (this.showEditForm) {
      let formattedDate = '';

      if (this.announcement.eventDate) {
        try {
          const dateVal = new Date(this.announcement.eventDate);
          if (!isNaN(dateVal.getTime())) {
            formattedDate = dateVal.toISOString().split('T')[0];
          } else {
            formattedDate = String(this.announcement.eventDate).substring(0, 10);
          }
        } catch (error) {
          console.error('Erro ao processar data para o formulário', error);
        }
      }

      this.editForm.patchValue({
        title: this.announcement.title,
        description: this.announcement.description,
        date: formattedDate,
        location: this.announcement.location || '',
      });
    }
  }

  validateEditFields(): boolean {
    return this.editForm.valid;
  }

  saveEdit(): void {
    this.editValidationFailed = false;

    if (!this.validateEditFields() || !this.announcement?.id) {
      console.error('Falha na validação ou ID ausente no anúncio');
      this.editValidationFailed = true;
      return;
    }

    const formValues = this.editForm.value;

    const updatedAnnouncement: Announcement = {
      ...this.announcement,
      id: this.announcement.id,
      title: formValues.title,
      description: formValues.description,
      eventDate: formValues.date || undefined,
      location: formValues.location || undefined,
    };

    this.announcementUpdateService.update(updatedAnnouncement).subscribe({
      next: (saved) => {
        const finalizedResult = (saved && typeof saved === 'object')
          ? { ...saved, id: this.announcement.id }
          : updatedAnnouncement;

        this.announcement = finalizedResult;
        this.showEditForm = false;

        this.updated.emit(finalizedResult);
      },
      error: (error) => {
        console.error('Erro ao atualizar campanha', error);
        this.editValidationFailed = true;
      },
    });
  }
} 