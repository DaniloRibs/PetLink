import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from '../../../../services/security/authentication';
import { CurrentUserService } from '../../../../services/security/current-user';
import { User } from '../../../../models/domain/user';
import { NotificationBell } from '../../notification/notification-bell/notification-bell';

@Component({
  selector: 'app-dashboard-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NotificationBell],
  templateUrl: './dashboard-layout.html',
  styleUrl: './dashboard-layout.css',
})
export class DashboardLayout implements OnInit {

  userEmail: string = '';
  currentUser: User | null = null;
  userMenuOpen: boolean = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private currentUserService: CurrentUserService,
  ) {
    try {
      this.userEmail = this.authenticationService.getAuthenticatedUserEmail();
    } catch {
      this.userEmail = '';
    }
  }

  async ngOnInit(): Promise<void> {
    this.currentUser = await this.currentUserService.load();
  }

  toggleUserMenu(): void {
    this.userMenuOpen = !this.userMenuOpen;
  }

  closeUserMenu(): void {
    this.userMenuOpen = false;
  }

  // Fecha o menu do usuário ao clicar fora dele.
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.userMenuOpen) {
      return;
    }
    const target = event.target as HTMLElement;
    if (!target.closest('.user-menu')) {
      this.userMenuOpen = false;
    }
  }

  logout(): void {
    this.authenticationService.logout();
    this.currentUserService.clear();
    this.router.navigate(['/account/sign-in']);
  }
}
