import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserPasswordUpdateService {

  constructor(private http: HttpClient) { }

  updatePassword(id: number, oldPassword: string, newPassword: string): Promise<void> {
    return firstValueFrom(
      this.http.patch<void>(`${environment.api_endpoint}/user/update-password`, {
        id,
        oldPassword,
        newPassword,
      })
    );
  }
}
