import { speciesIcon } from '../../../../shared/pet-species-icon';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { Pet } from '../../../../models/domain/pet';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetUpdateService } from '../../../../services/pet/pet-update';
import { UserReadService } from '../../../../services/user/user-read';
import { CurrentUserService } from '../../../../services/security/current-user';

@Component({
  selector: 'app-adoption-hub',
  imports: [],
  templateUrl: './adoption-hub.html',
  styleUrl: './adoption-hub.css',
})
export class AdoptionHub implements OnInit {

  activeTab: 'adotar' | 'doar' = 'adotar';

  availableForAdoption: Pet[] = [];
  myPets: Pet[] = [];
  loading: boolean = true;
  userId: number | null = null;
  ownerEmails: Record<number, string> = {};
  pendingConfirmationId: number | null = null;

  speciesIcon(species: string): string {
    return speciesIcon(species);
  }


  constructor(
    private petReadService: PetReadService,
    private petUpdateService: PetUpdateService,
    private userReadService: UserReadService,
    private currentUserService: CurrentUserService,
    private cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }
      this.userId = currentUser.id;

      const allPets = await this.petReadService.findAll();

      this.availableForAdoption = allPets.filter(
        p => p.forAdoption && p.ownerId !== this.userId
      );
      this.myPets = allPets.filter(p => p.ownerId === this.userId);

      await this.loadOwnerEmails(this.availableForAdoption);
    } catch (error) {
      console.error('Erro ao carregar dados de adoção', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }
  private async loadOwnerEmails(pets: Pet[]): Promise<void> {
    const uniqueOwnerIds = [...new Set(pets.map(p => p.ownerId))];

    const owners = await Promise.all(
      uniqueOwnerIds.map(ownerId =>
        this.userReadService.findById(ownerId).catch(error => {
          console.error(`Erro ao buscar dono ${ownerId}`, error);
          return null;
        })
      )
    );

    owners.forEach((owner, index) => {
      if (owner) {
        this.ownerEmails[uniqueOwnerIds[index]] = owner.email;
      }
    });
  }

  ownerContact(pet: Pet): string {
    return this.ownerEmails[pet.ownerId] ?? 'Contato indisponível';
  }

  setTab(tab: 'adotar' | 'doar'): void {
    this.activeTab = tab;
  }

  requestToggleDonation(pet: Pet): void {
    this.pendingConfirmationId = pet.id ?? null;
  }

  cancelToggleDonation(): void {
    this.pendingConfirmationId = null;
  }

  confirmToggleDonation(pet: Pet): void {
    pet.forAdoption = !pet.forAdoption;
    this.pendingConfirmationId = null;

    this.petUpdateService.update(pet).subscribe({
      error: (error) => console.error('Erro ao atualizar pet', error),
    });
  }
}
