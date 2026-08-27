import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pet } from '../../models/domain/pet';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PetCreateService {

  constructor(private http: HttpClient) { }

  create(pet: Pet): Observable<Pet> {
    return this.http.post<Pet>(`${environment.api_endpoint}/pets`, pet);
  }
}
