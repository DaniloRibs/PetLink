
import { Component } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Announcement } from '../../../models/domain/announcement';


@Component({
  selector: 'app-announcement-list',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './announcement-list.component.html',
  styleUrl: './announcement-list.component.scss'
})
export class AnnouncementListComponent {

  // Lista mockada para exibir os cards (substitua futuramente pela chamada da API/Service)
  announcements: Announcement[] = [
    {
      title: 'Campanha de Vacinação Antirrábica 2026',
      description: 'Traga seu cão ou gato para se vacinar gratuitamente contra a raiva. Não se esqueça da carteirinha!',
      dateAnnouncement: '2026-09-15',
     
      idUser: 1,

    },
    {
      title: 'Reforço V10 e Antirrábica para Cães',
      description: 'Dia D de vacinação no parque central. Atendimento por ordem de chegada para todos os portes.',
      dateAnnouncement: '2026-10-02',
      
      idUser: 2,

    }
  ];

  verDetalhes(announcement: Announcement): void {
    console.log('Detalhes do anúncio selecionado:', announcement);
    // Adicione aqui a navegação ou abertura de dialog/modal
  }

}