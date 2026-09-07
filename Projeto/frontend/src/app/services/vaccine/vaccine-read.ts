import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Vaccine } from '../../models/domain/vaccine';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccineReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<Vaccine[]> {
    return firstValueFrom(this.http.get<Vaccine[]>(`${environment.api_endpoint}/vaccines`));
  }

  findByPetId(petId: string): Promise<Vaccine[]> {
    return firstValueFrom(
      this.http.get<Vaccine[]>(`${environment.api_endpoint}/vaccines?petId=${petId}`)
    );
  }
}
