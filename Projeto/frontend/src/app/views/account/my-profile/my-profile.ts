// import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { ActivatedRoute, RouterModule } from '@angular/router';
// import { UserReadService } from '../../../services/user/user-read';
// import { User } from '../../../models/domain/user';
// import { UserCredentialDto } from '../../../models/dto/user-credential-dto';
// import { AuthenticationService } from '../../../services/security/authentication';

// @Component({
//   selector: 'app-my-profile',
//   imports: [
//     RouterModule,
//     FormsModule,
//     ReactiveFormsModule
//   ],
//   templateUrl: './my-profile.html',
//   styleUrl: './my-profile.css',
// })
// export class MyProfile implements OnInit {
//   entity?: User;


//    constructor(
//     private readService: UserReadService,
//     private route: ActivatedRoute,
//     private authenticationService: AuthenticationService,
//     private cdr: ChangeDetectorRef,

//   ){}


//   ngOnInit(): void {

//     let entityEmail = this.authenticationService.getAuthenticatedUserEmail()
//     console.log(`id da entidade: ${entityEmail}`);

//     this.loadEntityByEmail(entityEmail!);
//   }

//     async loadEntityByEmail(entityEmail: string){
//       this.entity = await this.readService.findByEmail(entityEmail);
//       console.log(this.entity);
//       this.cdr.detectChanges();

//     }


// }
