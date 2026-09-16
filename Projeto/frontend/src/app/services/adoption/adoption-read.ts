import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Adoption } from '../../models/domain/adoption';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdoptionReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<Adoption[]> {
    return firstValueFrom(this.http.get<Adoption[]>(`${environment.api_endpoint}/adoption`));
  }

  findById(id: number): Promise<Adoption> {
    return firstValueFrom(this.http.get<Adoption>(`${environment.api_endpoint}/adoption/${id}`));
  }
}
