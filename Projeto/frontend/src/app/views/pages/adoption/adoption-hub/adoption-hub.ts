import { speciesIcon } from '../../../../shared/pet-species-icon';
import { FormsModule, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { forkJoin } from 'rxjs';
import { speciesLabel } from '../../../../shared/pet-options';
import { Pet } from '../../../../models/domain/pet';
import { Adoption } from '../../../../models/domain/adoption';
import { CreateAdoptionDto } from '../../../../models/dto/create-adoption-dto';
import { User } from '../../../../models/domain/user';
import { isValidPhone } from '../../../../shared/document-validators';
import { PetReadService } from '../../../../services/pet/pet-read';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AdoptionReadService } from '../../../../services/adoption/adoption-read';
import { AdoptionCreateService } from '../../../../services/adoption/adoption-create';
import { AdoptionDeleteService } from '../../../../services/adoption/adoption-delete';
import { AdoptionMarkAsAdoptedService } from '../../../../services/adoption/adoption-mark-as-adopted';
import { AdoptionTransferPendingReadService } from '../../../../services/adoption/adoption-transfer-pending-read';
import { AdoptionTransferDecisionService } from '../../../../services/adoption/adoption-transfer-decision';
import { AdoptionUpdateService } from '../../../../services/adoption/adoption-update';

interface AdoptionListing {
  adoption: Adoption;
  pet: Pet;
}

@Component({
  selector: 'app-adoption-hub',
  imports: [MatIconModule, ReactiveFormsModule, FormsModule],
  templateUrl: './adoption-hub.html',
  styleUrl: './adoption-hub.css',
})
export class AdoptionHub implements OnInit {

  activeTab: 'adotar' | 'doar' = 'adotar';
  loading: boolean = true;
  userId: number | null = null;
  user: User | null = null;

  myPets: Pet[] = [];
  myAdoptionsByPetId: Record<number, Adoption> = {};
  sellablePets: Pet[] = [];
  myDonations: AdoptionListing[] = [];
  availableForAdoption: AdoptionListing[] = [];
  pendingTransfers: AdoptionListing[] = [];
  transferDecisionFailed: boolean = false;

  selectedAdoption: AdoptionListing | null = null;
  confirmingRemoval: boolean = false;
  removeFailed: boolean = false;
  markAdoptedFailed: boolean = false;

  selectedPetId: number | null = null;
  showAdoptionForm: boolean = false;
  donationForm: FormGroup;
  donationValidationFailed: boolean = false;

  speciesIcon(species: string): string {
    return speciesIcon(species);
  }

  speciesLabel(species: string): string {
    return speciesLabel(species);
  }

  get userHasValidPhone(): boolean {
    return isValidPhone(this.user?.phone ?? '');
  }

  constructor(
    private petReadService: PetReadService,
    private currentUserService: CurrentUserService,
    private adoptionReadService: AdoptionReadService,
    private adoptionCreateService: AdoptionCreateService,
    private adoptionDeleteService: AdoptionDeleteService,
    private adoptionMarkAsAdoptedService: AdoptionMarkAsAdoptedService,
    private adoptionTransferPendingReadService: AdoptionTransferPendingReadService,
    private adoptionTransferDecisionService: AdoptionTransferDecisionService,
    private adoptionUpdateService: AdoptionUpdateService,
    private formBuilder: FormBuilder,
    private toastrService: ToastrService,
    private cdr: ChangeDetectorRef,
  ) {
    this.donationForm = this.formBuilder.group({
      description: ['', [Validators.required]],
      contactMethod: ['email', [Validators.required]],
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      this.user = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!this.user?.id) {
        throw new Error('Usuário atual não encontrado');
      }
      this.userId = this.user.id;

      const [myPets, allAdoptions] = await Promise.all([
        this.petReadService.findByOwnerId(this.userId),
        this.adoptionReadService.findAll(),
      ]);

      this.myPets = myPets;

      this.myAdoptionsByPetId = {};
      allAdoptions
        .filter(adoption => adoption.ownerId === this.userId && !adoption.adopted)
        .forEach(adoption => { this.myAdoptionsByPetId[adoption.petId] = adoption; });

      this.sellablePets = myPets.filter(pet => !this.hasActiveDonation(pet.id));
      this.myDonations = myPets
        .filter(pet => this.hasActiveDonation(pet.id))
        .map(pet => ({ pet, adoption: this.myAdoptionsByPetId[pet.id!] }));

      const othersAdoptions = allAdoptions.filter(
        adoption => adoption.ownerId !== this.userId && !adoption.adopted
      );

      const listings = await this.loadListings(othersAdoptions);
      this.availableForAdoption = listings.sort((a, b) => {
        const aTime = a.adoption.publicationDate ? new Date(a.adoption.publicationDate).getTime() : 0;
        const bTime = b.adoption.publicationDate ? new Date(b.adoption.publicationDate).getTime() : 0;
        return aTime - bTime;
      });
      const pending = await this.adoptionTransferPendingReadService.findPendingByReceiverId(this.userId).catch(() => []);
      this.pendingTransfers = await this.loadListings(pending);
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

  waitingLabel(publicationDate?: string): string | null {
    if (!publicationDate) {
      return null;
    }
    const published = new Date(publicationDate).getTime();
    if (isNaN(published)) {
      return null;
    }
    const days = Math.floor((Date.now() - published) / (1000 * 60 * 60 * 24));
    if (days <= 0) {
      return 'Novo hoje';
    }
    return days === 1 ? '1 dia esperando' : `${days} dias esperando`;
  }

  contactIcon(contact?: string): string {
    return contact?.includes('@') ? 'mail' : 'call';
  }

  setTab(tab: 'adotar' | 'doar'): void {
    this.activeTab = tab;
  }

  hasActiveDonation(petId: number | undefined): boolean {
    return !!petId && !!this.myAdoptionsByPetId[petId];
  }


  openAdoptionForm(): void {
    this.selectedPetId = null;
    this.donationValidationFailed = false;
    this.donationForm.reset({ description: '', contactMethod: 'email' });
    this.showAdoptionForm = true;
  }

  closeAdoptionForm(): void {
    this.showAdoptionForm = false;
  }

  selectPet(id: number | undefined): void {
    if (id === undefined) {
      return;
    }
    this.selectedPetId = id;
  }

  isPetSelected(id: number | undefined): boolean {
    return id !== undefined && this.selectedPetId === id;
  }

  validateDonationFields(): boolean {
    return this.donationForm.valid && this.selectedPetId !== null;
  }

  confirmOfferForAdoption(): void {
    this.donationValidationFailed = false;

    if (!this.userId || !this.user || !this.validateDonationFields()) {
      this.donationValidationFailed = true;
      return;
    }

    const wantsPhone = this.donationForm.controls['contactMethod'].value === 'phone';
    const contact = (wantsPhone && this.userHasValidPhone) ? this.user.phone! : this.user.email;

    const createAdoptionDto: CreateAdoptionDto = {
      petId: this.selectedPetId!,
      ownerId: this.userId,
      description: this.donationForm.controls['description'].value,
      contact,
    };

    this.adoptionCreateService.create(createAdoptionDto).subscribe({
      next: () => {
        this.showAdoptionForm = false;
        this.toastrService.success('Adoção publicada com sucesso!');
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao publicar adoção', error);
        this.donationValidationFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  openAdoption(listing: AdoptionListing): void {
    this.selectedAdoption = listing;
    this.confirmingRemoval = false;
    this.removeFailed = false;
    this.markAdoptedFailed = false;
  }

  closeAdoption(): void {
    this.selectedAdoption = null;
    this.confirmingRemoval = false;
  }

  requestRemoveFromList(): void {
    this.confirmingRemoval = true;
    this.removeFailed = false;
  }

  cancelRemoveFromList(): void {
    this.confirmingRemoval = false;
  }

  confirmRemoveFromList(): void {
    const adoptionId = this.selectedAdoption?.adoption.id;
    if (adoptionId === undefined) {
      return;
    }

    this.adoptionDeleteService.delete(adoptionId).subscribe({
      next: () => {
        this.closeAdoption();
        this.toastrService.success('Anúncio removido da lista.');
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao remover anúncio de adoção', error);
        this.removeFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  markingAdopted: boolean = false;
  receiverEmailInput: string = '';

  requestMarkAsAdopted(): void {
    this.markingAdopted = true;
    this.markAdoptedFailed = false;
    this.receiverEmailInput = '';
  }

  cancelMarkAsAdopted(): void {
    this.markingAdopted = false;
    this.receiverEmailInput = '';
  }

  confirmMarkAsAdopted(): void {
    const adoptionId = this.selectedAdoption?.adoption.id;
    if (adoptionId === undefined || !this.receiverEmailInput.trim()) {
      this.markAdoptedFailed = true;
      return;
    }

    this.markAdoptedFailed = false;
    this.adoptionMarkAsAdoptedService.markAsAdopted(adoptionId, this.receiverEmailInput.trim()).subscribe({
      next: () => {
        this.markingAdopted = false;
        this.closeAdoption();
        this.toastrService.success('Solicitação de transferência enviada! Aguardando confirmação do novo tutor.');
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao solicitar transferência', error);
        this.markAdoptedFailed = true;
        this.cdr.detectChanges();
      },
    });
  }
  acceptTransfer(listing: AdoptionListing): void {
    if (!this.userId || listing.adoption.id === undefined) {
      return;
    }

    this.transferDecisionFailed = false;
    this.adoptionTransferDecisionService.confirm(listing.adoption.id, this.userId).subscribe({
      next: () => {
        this.toastrService.success(`${listing.pet.name} agora é seu!`);
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao confirmar transferência', error);
        this.transferDecisionFailed = true;
        this.cdr.detectChanges();
      },
    });
  }

  rejectTransfer(listing: AdoptionListing): void {
    if (!this.userId || listing.adoption.id === undefined) {
      return;
    }

    this.transferDecisionFailed = false;
    this.adoptionTransferDecisionService.reject(listing.adoption.id, this.userId).subscribe({
      next: () => {
        this.toastrService.info('Transferência recusada.');
        this.reload();
      },
      error: (error) => {
        console.error('Erro ao recusar transferência', error);
        this.transferDecisionFailed = true;
        this.cdr.detectChanges();
      },
    });
  }
  acknowledgeRejection(listing: AdoptionListing): void {
    if (listing.adoption.id === undefined) {
      return;
    }

    const updatedAdoption: Adoption = { ...listing.adoption, transferStatus: 'NONE' };

    this.adoptionUpdateService.update(updatedAdoption).subscribe({
      next: () => this.reload(),
      error: (error) => console.error('Erro ao reconhecer recusa da transferência', error),
    });
  }
}