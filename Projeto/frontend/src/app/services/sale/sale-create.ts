import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FarmAnimalSale } from '../../models/domain/farmAnimalSale';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleCreateService {

    constructor(private http: HttpClient) { }

    create(sale: FarmAnimalSale): Observable<void> {
        return this.http.post<void>(`${environment.api_endpoint}/farm-animal-sale`, sale);
    }
}