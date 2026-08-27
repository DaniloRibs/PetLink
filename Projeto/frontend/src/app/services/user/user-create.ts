import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/domain/user';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserCreateService {

  constructor(private http: HttpClient) { }

  create(user: User) {
    return this.http.post(`${environment.api_endpoint}/user`, user);
  }
}
