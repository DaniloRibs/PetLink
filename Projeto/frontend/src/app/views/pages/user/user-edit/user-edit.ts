import { ToastrService } from 'ngx-toastr';
import { User } from './../../../../models/domain/user';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserReadService } from '../../../../services/user/user-read';
import { UserUpdateService } from '../../../../services/user/user-update';

@Component({
  selector: 'app-user-edit',
  imports: [
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
})
export class UserEdit implements OnInit {

entityId: string = '-1';
form: FormGroup;
fullnameMiniLength: number = 2;
fullnameMaxLength: number = 14;




constructor(
    private cdr: ChangeDetectorRef,
    private readService: UserReadService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private updateService: UserUpdateService,
    private toastrService: ToastrService,
  ){
    this.inicializeForm();
  }

  inicializeForm(){
    this.form = this.formBuilder.group({
      fullname: ['', [
    Validators.required,
    Validators.minLength(this.fullnameMiniLength),
    Validators.maxLength(this.fullnameMaxLength),

    ]]
  });
  }

  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    if(id == null || id == ''){
      console.log('ID invalido');
      return;
    }
    this.entityId = id;
    console.log(`ID da entidade: ${id!}`);

    this.loadEntityById(id);
  }

  async loadEntityById(id: string){
    let entity = await this.readService.findById(id);
    console.log(entity);

    this.form.controls['fullname'].setValue(entity.fullname)

  }

  validateFields(){
    return this.form.controls['fullname'].valid;
  }

  async update(){
    console.log('atualizandp dados...')

    let fullname = this.form.controls['fullname'].value;
    console.log(`nome digigtado: ${fullname}`);


    try {
      await this.updateService.update(this.entityId, fullname);


      this.toastrService.success('Dados salvos com sucesso!');
      this.router.navigate(['user/list']);
    } catch (error: any) {
      this.toastrService.error(error.mensage);

    }

  }

}
