import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Adoption } from '../../models/domain/adoption';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdoptionUpdateService {

  constructor(private http: HttpClient) { }

  update(adoption: Adoption): Observable<void> {
    return this.http.put<void>(`${environment.api_endpoint}/adoption/${adoption.id}`, adoption);
  }
}
