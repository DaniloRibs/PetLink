import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { UserReadService } from './user-read';
import { User } from '../../models/domain/user';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserUpdateService {

  constructor(
    private http: HttpClient,
    private readService: UserReadService,
  ) { }

  async update(id: string, fullname: string): Promise<any> {
    let data: User = await this.readService.findById(id);

    if (data == null) {
      throw new Error('Dados não encontrados');
    }

    data.fullname = fullname;

    return firstValueFrom(this.http.put<any>(`${environment.api_endpoint}/user/${id}`, data));
  }
}
