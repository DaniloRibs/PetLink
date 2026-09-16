import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdoptionMarkAsAdoptedService {

  constructor(private http: HttpClient) { }

  markAsAdopted(id: number): Observable<void> {
    return this.http.patch<void>(`${environment.api_endpoint}/adoption/${id}/adopt`, {});
  }
}
