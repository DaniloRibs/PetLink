import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vaccine } from '../../models/domain/vaccine';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class VaccineUpdateService {

    constructor(private http: HttpClient) { }

    update(vaccine: Vaccine): Observable<Vaccine> {
        return this.http.put<Vaccine>(`${environment.api_endpoint}/pet/vaccine/${vaccine.id}`, vaccine);
    }
}