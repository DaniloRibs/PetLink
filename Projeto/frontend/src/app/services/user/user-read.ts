import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/domain/user';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<any> {
    return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user`));
  }

  findById(id: string): Promise<User> {
    return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user/${id}`));
  }

  findByEmail(email: string): Promise<User[]> {
    return firstValueFrom(this.http.get<User[]>(`${environment.api_endpoint}/user?email=${email}`));
  }
}
