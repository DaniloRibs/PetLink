import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class AdoptionTransferDecisionService {

    constructor(private http: HttpClient) { }

    confirm(adoptionId: number, receiverId: number): Observable<void> {
        return this.http.patch<void>(`${environment.api_endpoint}/adoption/${adoptionId}/transfer/confirm`, { receiverId });
    }

    reject(adoptionId: number, receiverId: number): Observable<void> {
        return this.http.patch<void>(`${environment.api_endpoint}/adoption/${adoptionId}/transfer/reject`, { receiverId });
    }
} 