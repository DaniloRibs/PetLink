import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatList, MatListModule } from '@angular/material/list';
import { MatMenu, MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import * as fontawsome from '@fortawesome/free-solid-svg-icons'
import { AuthenticationService } from '../../../services/security/authentication';

@Component({
  selector: 'app-main',
  imports: [
    RouterModule,
    RouterOutlet,
    MatToolbarModule,
    FormsModule,
    MatButtonModule,
    MatSidenavModule,
    MatMenuModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    MatTooltipModule,
    FontAwesomeModule,

  ],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  faFooterIcon = fontawsome.faHeartBroken

  constructor(private router: Router,
    private authenticationService: AuthenticationService,
  ){}

    logout(){
      console.log("Saindo do app");
      this.authenticationService.logout();
      this.router.navigate(['account/sign-in']);
  }

}
