import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pet } from '../../models/domain/pet';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PetUpdateService {

  constructor(private http: HttpClient) { }

  update(pet: Pet): Observable<Pet> {
    return this.http.put<Pet>(`${environment.api_endpoint}/pets/${pet.id}`, pet);
  }
}
