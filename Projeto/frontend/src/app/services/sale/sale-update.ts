import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FarmAnimalSale } from '../../models/domain/farmAnimalSale';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleUpdateService {

    constructor(private http: HttpClient) { }

    update(sale: FarmAnimalSale): Observable<void> {
        return this.http.put<void>(`${environment.api_endpoint}/animal-sale/${sale.id}`, sale);
    }
} 