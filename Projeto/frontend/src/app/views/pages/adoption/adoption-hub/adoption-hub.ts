import { speciesIcon } from '../../../../shared/pet-species-icon';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Pet } from '../../../../models/domain/pet';
import { Adoption } from '../../../../models/domain/adoption';
import { CreateAdoptionDto } from '../../../../models/dto/create-adoption-dto';
import { PetReadService } from '../../../../services/pet/pet-read';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AdoptionReadService } from '../../../../services/adoption/adoption-read';
import { AdoptionCreateService } from '../../../../services/adoption/adoption-create';
import { AdoptionDeleteService } from '../../../../services/adoption/adoption-delete';
import { AdoptionMarkAsAdoptedService } from '../../../../services/adoption/adoption-mark-as-adopted';

interface AdoptionListing {
  adoption: Adoption;
  pet: Pet;
}

@Component({
  selector: 'app-adoption-hub',
  imports: [MatIconModule, ReactiveFormsModule],
  templateUrl: './adoption-hub.html',
  styleUrl: './adoption-hub.css',
})
export class AdoptionHub implements OnInit {

  activeTab: 'adotar' | 'doar' = 'adotar';

  availableForAdoption: AdoptionListing[] = [];
  myPets: Pet[] = [];
  myAdoptionsByPetId: Record<number, Adoption> = {};
  loading: boolean = true;
  userId: number | null = null;

  donationForm: FormGroup;
  petIdBeingOffered: number | null = null;
  donationValidationFailed: boolean = false;

  pendingRemovalAdoptionId: number | null = null;

  speciesIcon(species: string): string {
    return speciesIcon(species);
  }

  constructor(
    private petReadService: PetReadService,
    private currentUserService: CurrentUserService,
    private adoptionReadService: AdoptionReadService,
    private adoptionCreateService: AdoptionCreateService,
    private adoptionDeleteService: AdoptionDeleteService,
    private adoptionMarkAsAdoptedService: AdoptionMarkAsAdoptedService,
    private formBuilder: FormBuilder,
    private cdr: ChangeDetectorRef,
  ) {
    this.donationForm = this.formBuilder.group({
      description: ['', [Validators.required]],
      contact: ['', [Validators.required]],
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }
      this.userId = currentUser.id;

      const [myPets, allAdoptions] = await Promise.all([
        this.petReadService.findByOwnerId(this.userId),
        this.adoptionReadService.findAll(),
      ]);

      this.myPets = myPets;

      this.myAdoptionsByPetId = {};
      allAdoptions
        .filter(adoption => adoption.ownerId === this.userId && !adoption.adopted)
        .forEach(adoption => { this.myAdoptionsByPetId[adoption.petId] = adoption; });

      const othersAdoptions = allAdoptions.filter(
        adoption => adoption.ownerId !== this.userId && !adoption.adopted
      );

      this.availableForAdoption = await this.loadListings(othersAdoptions);
    } catch (error) {
      console.error('Erro ao carregar dados de adoção', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  private async loadListings(adoptions: Adoption[]): Promise<AdoptionListing[]> {
    const listings = await Promise.all(
      adoptions.map(async adoption => {
        try {
          const pet = await this.petReadService.findById(String(adoption.petId));
          return { adoption, pet };
        } catch (error) {
          console.error(`Erro ao buscar pet ${adoption.petId}`, error);
          return null;
        }
      })
    );

    return listings.filter((listing): listing is AdoptionListing => listing !== null);
  }

  private reload(): void {
    this.loading = true;
    this.cdr.detectChanges();
    void this.ngOnInit();
  }

  setTab(tab: 'adotar' | 'doar'): void {
    this.activeTab = tab;
  }

  hasActiveDonation(petId: number | undefined): boolean {
    return !!petId && !!this.myAdoptionsByPetId[petId];
  }

  activeDonationFor(petId: number | undefined): Adoption | null {
    return petId ? this.myAdoptionsByPetId[petId] ?? null : null;
  }

  requestOfferForAdoption(pet: Pet): void {
    this.petIdBeingOffered = pet.id ?? null;
    this.donationValidationFailed = false;
    this.donationForm.reset();
  }

  cancelOfferForAdoption(): void {
    this.petIdBeingOffered = null;
  }

  validateDonationFields(): boolean {
    return this.donationForm.valid;
  }

  confirmOfferForAdoption(pet: Pet): void {
    this.donationValidationFailed = false;

    if (!this.userId || !pet.id || !this.validateDonationFields()) {
      this.donationValidationFailed = true;
      return;
    }

    const createAdoptionDto: CreateAdoptionDto = {
      petId: pet.id,
      ownerId: this.userId,
      description: this.donationForm.controls['description'].value,
      contact: this.donationForm.controls['contact'].value,
    };

    this.adoptionCreateService.create(createAdoptionDto).subscribe({
      next: () => {
        this.petIdBeingOffered = null;
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao publicar adoção', error);
        this.donationValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  requestMarkAsAdopted(adoptionId: number): void {
    this.adoptionMarkAsAdoptedService.markAsAdopted(adoptionId).subscribe({
      next: () => this.reload(),
      error: (error) => console.error('Erro ao marcar adoção como concluída', error),
    });
  }

  requestRemoveFromList(adoptionId: number): void {
    this.pendingRemovalAdoptionId = adoptionId;
  }

  cancelRemoveFromList(): void {
    this.pendingRemovalAdoptionId = null;
  }

  confirmRemoveFromList(adoptionId: number): void {
    this.adoptionDeleteService.delete(adoptionId).subscribe({
      next: () => {
        this.pendingRemovalAdoptionId = null;
        this.reload();
      },
      error: (error) => console.error('Erro ao remover anúncio de adoção', error),
    });
  }
}
