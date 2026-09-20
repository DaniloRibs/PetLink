import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleDeleteService {

    constructor(private http: HttpClient) { }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${environment.api_endpoint}/animal-sale/${id}`);
    }
}