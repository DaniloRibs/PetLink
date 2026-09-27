import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleRequestClosureService {

    constructor(private http: HttpClient) { }

    requestClosure(id: number, buyerEmail: string): Observable<void> {
        return this.http.patch<void>(`${environment.api_endpoint}/animal-sale/${id}/close`, { buyerEmail });
    }
} 