import { CommonModule, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Announcement, AnnouncementType } from '../../../../models/domain/announcement';
import { AnnouncementUpdateService } from '../../../../services/announcement/announcement-update';

@Component({
  selector: 'announcement-card',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule
  ],
  templateUrl: './announcement-card.html',
  styleUrl: './announcement-card.scss'
})

export class AnnouncementCardComponent {

  @Input({ required: true }) announcement!: Announcement;
  @Input() canManage: boolean = false;
  @Output() delete = new EventEmitter<Announcement>();
  @Output() updated = new EventEmitter<Announcement>();

  AnnouncementType = AnnouncementType;
  expanded = false;

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

  toggleDetails(): void {
    this.expanded = !this.expanded;
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
    this.editValidationFailed = false;

    if (this.showEditForm) {
      this.editForm.patchValue({
        title: this.announcement.title,
        description: this.announcement.description,
        date: this.announcement.date || '',
        location: this.announcement.location || '',
      });
    }
  }

  validateEditFields(): boolean {
    return this.editForm.valid;
  }

  saveEdit(): void {
    this.editValidationFailed = false;

    if (!this.validateEditFields()) {
      this.editValidationFailed = true;
      return;
    }

    const updatedAnnouncement: Announcement = {
      ...this.announcement,
      title: this.editForm.controls['title'].value,
      description: this.editForm.controls['description'].value,
      date: this.editForm.controls['date'].value || undefined,
      location: this.editForm.controls['location'].value || undefined,
    };

    this.announcementUpdateService.update(updatedAnnouncement).subscribe({
      next: (saved) => {
        this.announcement = saved;
        this.showEditForm = false;
        this.updated.emit(saved);
      },
      error: (error) => {
        console.error('Erro ao atualizar campanha', error);
        this.editValidationFailed = true;
      },
    });
  }
}