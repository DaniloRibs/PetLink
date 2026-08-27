import { Injectable } from '@angular/core';
import { User } from '../../models/domain/user';
import { UserReadService } from '../user/user-read';
import { AuthenticationService } from './authentication';

@Injectable({
  providedIn: 'root',
})
export class CurrentUserService {

  private cachedUser: User | null = null;

  constructor(
    private userReadService: UserReadService,
    private authenticationService: AuthenticationService,
  ) { }

  async load(): Promise<User | null> {
    try {
      const email = this.authenticationService.getAuthenticatedUserEmail();
      const users = await this.userReadService.findByEmail(email);
      const usersFound: User[] = Array.isArray(users) ? users : (users ? [users] : []);

      this.cachedUser = usersFound[0] ?? null;
      return this.cachedUser;
    } catch (error) {
      console.error('Erro ao carregar usuario logado', error);
      this.cachedUser = null;
      return null;
    }
  }

  get(): User | null {
    return this.cachedUser;
  }

  clear(): void {
    this.cachedUser = null;
  }
}
