import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from '../../../../services/security/authentication';

@Component({
  selector: 'app-dashboard-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './dashboard-layout.html',
  styleUrl: './dashboard-layout.css',
})
export class DashboardLayout {

  userEmail: string = '';

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {
    try {
      this.userEmail = this.authenticationService.getAuthenticatedUserEmail();
    } catch {
      this.userEmail = '';
    }
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/account/sign-in']);
  }
}
