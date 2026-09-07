
import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Announcement, AnnouncementType } from '../../../models/domain/announcement';

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

  // Exposto para o template poder comparar announcement.type
  AnnouncementType = AnnouncementType;

  verDetalhes(): void {
    console.log('Detalhes do anúncio selecionado:', this.announcement);
    // Adicione aqui a navegação ou abertura de dialog/modal
  }

}