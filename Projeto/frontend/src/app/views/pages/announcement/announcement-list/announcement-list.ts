import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

import { PetReadService } from '../../../../services/pet/pet-read';
import { VaccineReadService } from '../../../../services/vaccine/vaccine-read';
import { AuthenticationService } from '../../../../services/security/authentication';

interface AnnouncementItem {
  petId: string;
  petName: string;
  message: string;
  status: 'overdue' | 'soon' | 'missing';
}

@Component({
  selector: 'app-announcement-list',
  imports: [RouterLink],
  templateUrl: './announcement-list.html',
  styleUrl: './announcement-list.css',
})
export class AnnouncementList implements OnInit {

  announcements: AnnouncementItem[] = [];
  loading: boolean = true;

  constructor(
    private petReadService: PetReadService,
    private vaccineReadService: VaccineReadService,
    private authenticationService: AuthenticationService,
    private cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      const email = this.authenticationService.getAuthenticatedUserEmail();
      const [pets, vaccines] = await Promise.all([
        this.petReadService.findByOwnerEmail(email),
        this.vaccineReadService.findAll(),
      ]);

      const today = new Date();
      const items: AnnouncementItem[] = [];

      for (const pet of pets) {
        const petVaccines = vaccines.filter(v => v.petId === pet.id);

        if (petVaccines.length === 0) {
          items.push({
            petId: pet.id!,
            petName: pet.name,
            message: 'ainda não tem nenhuma vacina cadastrada.',
            status: 'missing',
          });
          continue;
        }

        for (const vaccine of petVaccines) {
          if (!vaccine.nextDoseDate) {
            continue;
          }

          const nextDose = new Date(vaccine.nextDoseDate);
          const diffDays = Math.ceil((nextDose.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

          if (diffDays < 0) {
            items.push({
              petId: pet.id!,
              petName: pet.name,
              message: `está com a dose de ${vaccine.name} atrasada.`,
              status: 'overdue',
            });
          } else if (diffDays <= 30) {
            items.push({
              petId: pet.id!,
              petName: pet.name,
              message: `tem dose de ${vaccine.name} prevista para daqui a ${diffDays} dia(s).`,
              status: 'soon',
            });
          }
        }
      }

      this.announcements = items;
    } catch (error) {
      console.error('Erro ao montar anúncios de vacina', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }
}
