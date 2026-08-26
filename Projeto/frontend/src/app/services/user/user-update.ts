import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { UserReadService } from './user-read';
import { User } from '../../models/domain/user';

@Injectable({
  providedIn: 'root',
})
export class UserUpdateService {

constructor(
  private http: HttpClient,
  private readService: UserReadService,
){

}
async update(id: string, fullname: string): Promise<any>{
  console.log(`id do usuario que será atulizado ${id}`);

  let data : User  = await this.readService.findById(id);

  if( data == null){
    throw new Error('Dados nâo encontrados');
  }

  data.fullname = fullname


  return firstValueFrom(this.http.put<any>(`http://localhost:3000/user/${id}`,data));


}


}
