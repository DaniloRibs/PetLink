import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserCredentialDto } from '../../models/dto/user-credential-dto';
import { from, map, Observable, switchMap, throwError } from 'rxjs';
import { User } from '../../models/domain/user';
import { UserReadService } from '../user/user-read';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private http: HttpClient,
    private userReadService: UserReadService,
  ) { }

  authenticate(credentials: UserCredentialDto): Observable<User> {
    return from(this.userReadService.findByEmail(credentials.email)).pipe(
      switchMap((users: any) => {
        const usersFound: User[] = Array.isArray(users) ? users : (users ? [users] : []);
        const user = usersFound.find(u => u.password === credentials.password);

        if (!user) {
          return throwError(() => new Error('Email e/ou senha incorretos'));
        }

        return [user];
      })
    );
  }

  isAuthenticated(): boolean {
    if (typeof window === 'undefined') {
      return false;
    }
    return localStorage.getItem('email') !== null;
  }

  logout(): void {
    localStorage.clear();
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
