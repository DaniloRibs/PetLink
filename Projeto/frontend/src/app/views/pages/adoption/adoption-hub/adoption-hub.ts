import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { Pet } from '../../../../models/domain/pet';
import { PetReadService } from '../../../../services/pet/pet-read';
import { PetUpdateService } from '../../../../services/pet/pet-update';
import { AuthenticationService } from '../../../../services/security/authentication';

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
  userEmail: string = '';
  pendingConfirmationId: string | null = null;

  constructor(
    private petReadService: PetReadService,
    private petUpdateService: PetUpdateService,
    private authenticationService: AuthenticationService,
    private cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      this.userEmail = this.authenticationService.getAuthenticatedUserEmail();
      const allPets = await this.petReadService.findAll();

      this.availableForAdoption = allPets.filter(
        p => p.forAdoption && p.ownerEmail !== this.userEmail
      );
      this.myPets = allPets.filter(p => p.ownerEmail === this.userEmail);
    } catch (error) {
      console.error('Erro ao carregar dados de adoção', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
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
