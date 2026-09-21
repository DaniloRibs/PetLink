import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { VaccineAlert } from '../../models/domain/vaccineAlert';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccineAlertReadService {

  constructor(private http: HttpClient) { }

  findByUserId(userId: number): Promise<VaccineAlert[]> {
    return firstValueFrom(
      this.http.get<VaccineAlert[]>(`${environment.api_endpoint}/user/${userId}/vaccine-alert`)
    );
  }
}
