import { ChangeDetectorRef, Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { Pet } from '../../../../models/domain/pet';
import { AnimalMode } from '../../../../models/domain/animalMode';
import { PetReadService } from '../../../../services/pet/pet-read';
import { FarmAnimalReadService } from '../../../../services/farm-animal/farm-animal-read';
import { VaccineReadService } from '../../../../services/vaccine/vaccine-read';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AnimalModeService } from '../../../../services/animalMode/animalMode';

interface VaccineNotification {
  key: string;
  petId: string;
  petName: string;
  message: string;
  status: 'overdue' | 'soon' | 'missing';
  kind: AnimalMode;
}

@Component({
  selector: 'app-notification-bell',
  imports: [RouterLink, MatIconModule],
  templateUrl: './notification-bell.html',
  styleUrl: './notification-bell.css',
})
export class NotificationBell implements OnInit {

  notifications: VaccineNotification[] = [];
  loading: boolean = true;
  panelOpen: boolean = false;

  private currentUserId: number | null = null;
  private readonly permanentKeyPrefix = 'petlink.keep.hiddenNotifications.';
  private readonly untilLoginKey = 'hiddenNotificationsUntilLogin';

  constructor(
    private petReadService: PetReadService,
    private farmAnimalReadService: FarmAnimalReadService,
    private vaccineReadService: VaccineReadService,
    private currentUserService: CurrentUserService,
    private animalModeService: AnimalModeService,
    private elementRef: ElementRef<HTMLElement>,
    private cdr: ChangeDetectorRef,
  ) { }

  get hasClearable(): boolean {
    return this.notifications.some(item => item.status !== 'overdue');
  }

  async ngOnInit(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }
      this.currentUserId = currentUser.id;

      const [pets, farmAnimals] = await Promise.all([
        this.petReadService.findByOwnerId(currentUser.id),
        this.farmAnimalReadService.findByOwnerId(currentUser.id).catch(error => {
          console.error('Erro ao carregar animais de fazenda', error);
          return [] as Pet[];
        }),
      ]);

      const farmIds = new Set(farmAnimals.map(animal => animal.id));
      const petsOnly = pets.filter(pet => !farmIds.has(pet.id));

      const [petItems, farmItems] = await Promise.all([
        this.buildNotifications(petsOnly, AnimalMode.PET),
        this.buildNotifications(farmAnimals, AnimalMode.FARM),
      ]);

      const hiddenForever = this.readSet(this.permanentStorageKey);
      const hiddenUntilLogin = this.readSet(this.untilLoginKey);

      this.notifications = [...petItems, ...farmItems].filter(item => {
        if (item.status === 'missing') {
          return !hiddenForever.has(item.key);
        }
        if (item.status === 'soon') {
          return !hiddenUntilLogin.has(item.key);
        }
        return true;
      });
    } catch (error) {
      console.error('Erro ao montar notificações de vacina', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  private async buildNotifications(animals: Pet[], kind: AnimalMode): Promise<VaccineNotification[]> {
    const today = new Date();

    const results = await Promise.all(animals.map(async animal => {
      const items: VaccineNotification[] = [];
      const name = animal.name || animal.identify || 'Animal sem nome';

      const vaccines = await this.vaccineReadService.findByPetId(String(animal.id)).catch(() => null);
      if (vaccines === null) {
        return items;
      }

      if (vaccines.length === 0) {
        items.push({
          key: `missing:${animal.id}`,
          petId: String(animal.id),
          petName: name,
          message: 'ainda não tem nenhuma vacina cadastrada.',
          status: 'missing',
          kind,
        });
        return items;
      }

      for (const vaccine of vaccines) {
        if (!vaccine.expirationDate) {
          continue;
        }

        const nextDose = new Date(vaccine.expirationDate);
        const diffDays = Math.ceil((nextDose.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
        const vaccineRef = vaccine.id ?? `${animal.id}-${vaccine.name}`;

        if (diffDays < 0) {
          items.push({
            key: `overdue:${vaccineRef}`,
            petId: String(animal.id),
            petName: name,
            message: `está com a dose de ${vaccine.name} atrasada.`,
            status: 'overdue',
            kind,
          });
        } else if (diffDays <= 30) {
          items.push({
            key: `soon:${vaccineRef}`,
            petId: String(animal.id),
            petName: name,
            message: `tem dose de ${vaccine.name} prevista para daqui a ${diffDays} dia(s).`,
            status: 'soon',
            kind,
          });
        }
      }

      return items;
    }));

    return results.flat();
  }

  clearNotifications(): void {
    const hiddenForever = this.readSet(this.permanentStorageKey);
    const hiddenUntilLogin = this.readSet(this.untilLoginKey);

    for (const item of this.notifications) {
      if (item.status === 'missing') {
        hiddenForever.add(item.key);
      } else if (item.status === 'soon') {
        hiddenUntilLogin.add(item.key);
      }
    }

    this.writeSet(this.permanentStorageKey, hiddenForever);
    this.writeSet(this.untilLoginKey, hiddenUntilLogin);

    this.notifications = this.notifications.filter(item => item.status === 'overdue');
  }

  private get permanentStorageKey(): string {
    return this.permanentKeyPrefix + this.currentUserId;
  }

  private readSet(storageKey: string): Set<string> {
    if (typeof localStorage === 'undefined') {
      return new Set<string>();
    }
    try {
      const raw = localStorage.getItem(storageKey);
      return new Set<string>(raw ? JSON.parse(raw) : []);
    } catch {
      return new Set<string>();
    }
  }

  private writeSet(storageKey: string, values: Set<string>): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    try {
      localStorage.setItem(storageKey, JSON.stringify([...values]));
    } catch (error) {
      console.error('Erro ao salvar avisos ocultos', error);
    }
  }

  openItem(item: VaccineNotification): void {
    this.animalModeService.set(item.kind);
    this.closePanel();
  }

  togglePanel(): void {
    this.panelOpen = !this.panelOpen;
  }

  closePanel(): void {
    this.panelOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.panelOpen) {
      return;
    }
    if (!this.elementRef.nativeElement.contains(event.target as Node)) {
      this.panelOpen = false;
    }
  }
}