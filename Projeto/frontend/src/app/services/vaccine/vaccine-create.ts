import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vaccine } from '../../models/domain/vaccine';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccineCreateService {

  constructor(private http: HttpClient) { }

  create(vaccine: Vaccine): Observable<Vaccine> {
    return this.http.post<Vaccine>(`${environment.api_endpoint}/vaccines`, vaccine);
  }
}
