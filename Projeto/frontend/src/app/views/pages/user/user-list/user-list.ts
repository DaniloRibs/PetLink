import { UserDeleteService } from './../../../../services/user/user-delete';
import { User } from './../../../../models/domain/user';

import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserReadService } from '../../../../services/user/user-read';
import { ToastrService } from 'ngx-toastr';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import * as fontawesome from '@fortawesome/free-solid-svg-icons'
import { RouterModule } from '@angular/router';
import { identity } from 'rxjs';

@Component({
  selector: 'app-user-list',
  imports: [
    RouterModule,
    FontAwesomeModule
  ],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {

  fa = fontawesome;

  constructor(
    private readService: UserReadService,
    private deleteService: UserDeleteService,
    private toastrService: ToastrService,
    private cdr: ChangeDetectorRef
  ){}

  users: User[] = [];

  ngOnInit(): void {
    this.loadEntities();
  }

  async loadEntities() {
    console.log('carregando entidades');

    let entities = await this.readService.findAll();
    if(entities == null) {
      console.log('nenhuma entidade encontrada');
      return;
    }

    console.log(entities);
    this.users = entities;

    console.log('entidades obtidas com sucesso');
    this.cdr.detectChanges();
  }

  async deleteEntity(entityId: string) {
    console.log('entidade removida');
  try{
    console.log(`Removido a entiddade com o id ${identity}`);

    await this.deleteService.delete(entityId)
    this.toastrService.success(`Removido com sucesso!`)
    this.loadEntities();

  }catch(error){
    console.error(error);
    this.toastrService.error(`Erro ao remover`)
  }

  }


}
