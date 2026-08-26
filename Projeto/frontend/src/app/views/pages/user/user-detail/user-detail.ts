import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { User } from '../../../../models/domain/user';
import { UserReadService } from '../../../../services/user/user-read';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-detail',
  imports: [
    RouterModule,
  ],
  templateUrl: './user-detail.html',
  styleUrl: './user-detail.css',
})
export class UserDetail implements OnInit {
  entity?: User;

  constructor(
    private readService: UserReadService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
  ){}

  ngOnInit(): void {

    let entityId = this.route.snapshot.paramMap.get('id');
    console.log(`id da entidade: ${entityId}`);

    this.loadEntityById(entityId!);
  }

    async loadEntityById(entityId: string){
      this.entity = await this.readService.findById(entityId);
      console.log(this.entity);
      this.cdr.detectChanges();

    }

}
