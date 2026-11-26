import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserCredentialDto } from '../../models/dto/user-credential-dto';
import { JwtTokenDto } from '../../models/dto/jwt-token-dto';
import { environment } from '../../../environments/environment';

interface JwtPayload {
  sub?: string;
  email?: string;
  fullname?: string;
  role?: string;
  exp?: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private http: HttpClient,
  ) { }

  authenticate(credentials: UserCredentialDto): Observable<JwtTokenDto> {
    return this.http.post<JwtTokenDto>(
      `${environment.authentication_api_endpoint}/authenticate`,
      credentials,
    );
  }

  isAuthenticated(): boolean {
    if (typeof window === 'undefined') {
      return false;
    }

    const token = this.getToken();
    if (token === null || localStorage.getItem('email') === null) {
      return false;
    }

    if (this.isTokenExpired(token)) {
      this.logout();
      return false;
    }
    return true;
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

  saveSession(token: string): boolean {
    const payload = this.decodeToken(token);
    const email = payload?.email ?? payload?.sub;

    if (!email) {
      return false;
    }

    localStorage.setItem('token', token);
    localStorage.setItem('email', email);
    return true;
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }
    return localStorage.getItem('token');
  }

  getAuthenticatedUserEmail(): string {
    let email = localStorage.getItem('email');
    if (email == null) {
      throw new Error('Email nao encontrado no cache');
    }
    return email;
  }

  private isTokenExpired(token: string): boolean {
    const exp = this.decodeToken(token)?.exp;
    if (exp === undefined) {
      return true;
    }
    return exp * 1000 <= Date.now();
  }

  private decodeToken(token: string): JwtPayload | null {
    try {
      const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      const bytes = Uint8Array.from(atob(base64), (char) => char.charCodeAt(0));
      return JSON.parse(new TextDecoder().decode(bytes)) as JwtPayload;
    } catch {
      return null;
    }
  }
}
