import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleClosureDecisionService {

    constructor(private http: HttpClient) { }

    confirm(saleId: number, buyerId: number): Observable<void> {
        return this.http.patch<void>(`${environment.api_endpoint}/animal-sale/${saleId}/close/confirm`, { buyerId });
    }

    reject(saleId: number, buyerId: number): Observable<void> {
        return this.http.patch<void>(`${environment.api_endpoint}/animal-sale/${saleId}/close/reject`, { buyerId });
    }
} 