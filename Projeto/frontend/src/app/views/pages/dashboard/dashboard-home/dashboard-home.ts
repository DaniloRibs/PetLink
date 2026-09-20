import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { filterByMode } from '../../../../shared/pet-options';

import { Pet } from '../../../../models/domain/pet';
import { AccountType } from '../../../../models/domain/user';
import { AnimalMode } from '../../../../models/domain/animalMode';
import { PetReadService } from '../../../../services/pet/pet-read';
import { AnnouncementReadService } from '../../../../services/announcement/announcement-read';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AnimalModeService } from '../../../../services/animalMode/animalMode';

@Component({
  selector: 'app-dashboard-home',
  imports: [RouterLink, MatIconModule],
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.css',
})
export class DashboardHome implements OnInit {

  userName: string = '';
  isCompany: boolean = false;

  allPets: Pet[] = [];
  adoptionCount: number = 0;
  campaignsCount: number = 0;
  adoptablePetsCount: number = 0;

  loading: boolean = true;

  get isFarm(): boolean {
    return this.animalModeService.get() === AnimalMode.FARM;
  }

  get petsCount(): number {
    return filterByMode(this.allPets, this.isFarm).length;
  }

  constructor(
    private petReadService: PetReadService,
    private announcementReadService: AnnouncementReadService,
    private currentUserService: CurrentUserService,
    private animalModeService: AnimalModeService,
    private cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      this.userName = currentUser?.fullname?.split(' ')[0] || '';
      this.isCompany = currentUser?.accountType === AccountType.ENTERPRISE;

      if (currentUser?.id) {
        const [myPets] = await Promise.all([
          this.petReadService.findByOwnerId(currentUser.id),
        ]);

        this.allPets = myPets;
        this.adoptionCount = myPets.filter((p: Pet) => p.forAdoption).length;
        this.adoptablePetsCount = myPets.filter((p: Pet) => p.forAdoption && p.ownerId !== currentUser.id).length;
      }

      if (this.isCompany) {
        const announcements = await this.announcementReadService.findAll();
        this.campaignsCount = announcements.filter(a => a.creatorEmail === currentUser?.email).length;
      }
    } catch (error) {
      console.error('Erro ao carregar resumo do painel', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }
}