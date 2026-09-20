import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserCredentialDto } from '../../models/dto/user-credential-dto';
import { AutheticatedUserDto } from '../../models/dto/autheticated-user-dto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private http: HttpClient,
  ) { }

  authenticate(credentials: UserCredentialDto): Observable<AutheticatedUserDto> {
    return this.http.post<AutheticatedUserDto>(
      `${environment.authentication_api_endpoint}/authenticate`,
      credentials,
    );
  }

  isAuthenticated(): boolean {
    if (typeof window === 'undefined') {
      return false;
    }
    return localStorage.getItem('email') !== null;
  }

  logout(): void {
    const preserved: [string, string][] = [];
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      if (key && key.startsWith('petlink.keep.')) {
        preserved.push([key, localStorage.getItem(key) ?? '']);
      }
    }

    localStorage.clear();
    preserved.forEach(([key, value]) => localStorage.setItem(key, value));
  }

  addDataToLocalStorage(email: string): void {
    localStorage.setItem('email', email);
  }

  getAuthenticatedUserEmail(): string {
    let email = localStorage.getItem('email');
    if (email == null) {
      throw new Error('Email nao encontrado no cache');
    }
    return email;
  }
}
