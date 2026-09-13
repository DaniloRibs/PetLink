
import { CommonModule, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Announcement, AnnouncementType } from '../../../../models/domain/announcement';

@Component({
  selector: 'announcement-card',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './announcement-card.html',
  styleUrl: './announcement-card.scss'
})

export class AnnouncementCardComponent {

  @Input({ required: true }) announcement!: Announcement;
  @Input() canManage: boolean = false;
  @Output() delete = new EventEmitter<Announcement>();

  onDelete(): void {
    this.delete.emit(this.announcement);
  }

  AnnouncementType = AnnouncementType;
  expanded = false;

  toggleDetails(): void {
    this.expanded = !this.expanded;
  }

}