import { ChangeDetectorRef, Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { AnimalMode } from '../../../../models/domain/animalMode';
import { VaccineAlert } from '../../../../models/domain/vaccineAlert';
import { VaccineAlertReadService } from '../../../../services/vaccine/vaccine-alert-read';
import { CurrentUserService } from '../../../../services/security/current-user';
import { AnimalModeService } from '../../../../services/animalMode/animalMode';

interface VaccineNotification {
  key: string;
  petId: string;
  petName: string;
  message: string;
  status: 'overdue' | 'soon';
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

  private readonly untilLoginKey = 'hiddenNotificationsUntilLogin';

  constructor(
    private vaccineAlertReadService: VaccineAlertReadService,
    private currentUserService: CurrentUserService,
    private animalModeService: AnimalModeService,
    private elementRef: ElementRef<HTMLElement>,
    private cdr: ChangeDetectorRef,
  ) { }

  get hasClearable(): boolean {
    return this.notifications.some(item => item.status !== 'overdue');
  }

  async ngOnInit(): Promise<void> {
    await this.loadNotifications();
  }

  private async loadNotifications(): Promise<void> {
    try {
      const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
      if (!currentUser?.id) {
        throw new Error('Usuário atual não encontrado');
      }

      const alerts = await this.vaccineAlertReadService.findByUserId(currentUser.id);
      const hiddenUntilLogin = this.readSet(this.untilLoginKey);

      this.notifications = alerts
        .map(alert => this.toNotification(alert))
        .filter(item => item.status === 'overdue' || !hiddenUntilLogin.has(item.key));
    } catch (error) {
      console.error('Erro ao carregar vacinas vencidas', error);
    } finally {
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  private toNotification(alert: VaccineAlert): VaccineNotification {
    const date = this.formatDate(alert.expirationDate);
    const overdue = alert.status === 'EXPIRED';

    let message: string;
    if (overdue) {
      const delay = alert.daysOverdue === 1 ? '1 dia de atraso' : `${alert.daysOverdue} dias de atraso`;
      message = `está com a vacina ${alert.vaccineName} vencida desde ${date} (${delay}).`;
    } else if (alert.daysUntilExpiration === 0) {
      message = `está com a vacina ${alert.vaccineName} vencendo hoje (${date}).`;
    } else {
      const remaining = alert.daysUntilExpiration === 1 ? '1 dia' : `${alert.daysUntilExpiration} dias`;
      message = `está com a vacina ${alert.vaccineName} para vencer em ${remaining} (${date}).`;
    }

    return {
      key: `${overdue ? 'overdue' : 'soon'}:${alert.animalId}:${alert.vaccineId}`,
      petId: String(alert.animalId),
      petName: alert.animalName || 'Animal sem nome',
      message,
      status: overdue ? 'overdue' : 'soon',
      kind: alert.farmAnimal ? AnimalMode.FARM : AnimalMode.PET,
    };
  }

  clearNotifications(): void {
    const hiddenUntilLogin = this.readSet(this.untilLoginKey);

    for (const item of this.notifications) {
      if (item.status === 'soon') {
        hiddenUntilLogin.add(item.key);
      }
    }

    this.writeSet(this.untilLoginKey, hiddenUntilLogin);
    this.notifications = this.notifications.filter(item => item.status === 'overdue');
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

  private formatDate(isoDate: string): string {
    const [year, month, day] = (isoDate ?? '').split('-');
    return year && month && day ? `${day}/${month}/${year}` : isoDate;
  }

  openItem(item: VaccineNotification): void {
    this.animalModeService.set(item.kind);
    this.closePanel();
  }

  togglePanel(): void {
    this.panelOpen = !this.panelOpen;

    if (this.panelOpen) {
      void this.loadNotifications();
    }
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
