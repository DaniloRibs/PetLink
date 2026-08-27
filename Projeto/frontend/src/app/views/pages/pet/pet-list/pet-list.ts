import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Pet } from '../../../../models/domain/pet';
import { PetReadService } from '../../../../services/pet/pet-read';
import { AuthenticationService } from '../../../../services/security/authentication';

@Component({
  selector: 'app-pet-list',
  imports: [],
  templateUrl: './pet-list.html',
  styleUrl: './pet-list.css',
})
export class PetList implements OnInit {

  pets: Pet[] = [];
  loading: boolean = true;

  constructor(
    private router: Router,
    private petReadService: PetReadService,
    private authenticationService: AuthenticationService,
    private cdr: ChangeDetectorRef,
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      const email = this.authenticationService.getAuthenticatedUserEmail();
      this.pets = await this.petReadService.findByOwnerEmail(email);
    } catch (error) {
      console.error('Erro ao carregar seus pets', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  openPet(pet: Pet): void {
    this.router.navigate(['/painel/pets', pet.id]);
  }

  speciesIcon(species: string): string {
    switch (species) {
      case 'cachorro': return '🐶';
      case 'gato': return '🐱';
      case 'passaro': return '🐦';
      default: return '🐾';
    }
  }
}
