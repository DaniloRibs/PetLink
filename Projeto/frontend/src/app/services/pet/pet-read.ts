import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Pet } from '../../models/domain/pet';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PetReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<Pet[]> {
    return firstValueFrom(this.http.get<Pet[]>(`${environment.api_endpoint}/pets`));
  }

  findByOwnerEmail(ownerEmail: string): Promise<Pet[]> {
    return firstValueFrom(
      this.http.get<Pet[]>(`${environment.api_endpoint}/pets?ownerEmail=${ownerEmail}`)
    );
  }

  findById(id: string): Promise<Pet> {
    return firstValueFrom(this.http.get<Pet>(`${environment.api_endpoint}/pets/${id}`));
  }
}
