import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserCredentialDto } from '../../models/dto/user-credential-dto';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private http: HttpClient) {

  }

  authenticate(credentials: UserCredentialDto): Observable<any> {
    console.log('Preparando para authenticar o usuario..');

    const headers = new HttpHeaders({ 'Content-Type': 'aplication/json' });

    const body = {
    }

    //Funciona somente no Json Server
    //comentar depois
    return this.http.get<any>(`${environment.authentication_api_endpoint}/user/1`);

  }

  isAuthenticated() {
    if (typeof window === 'undefined') {
      return false;
    }


    let email = localStorage.getItem('email');
    if (email != null) {
      console.log('email encontrado, realizando o login...');
      return true;
    }
    return false;
  }

  logout() {
    localStorage.clear();
  }

  addDataToLocalStorage(email: string) {
    console.log('adicionando dados no navegador');

    localStorage.setItem('email', email);

  }

  getAuthenticatedUserEmail() {
    let email = localStorage.getItem('email');
    if (email == null) {
      throw new Error('Email nao encotnrado no cache')
    }
    return email;
  }


}
