import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateAdoptionDto } from '../../models/dto/create-adoption-dto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AdoptionCreateService {

  constructor(private http: HttpClient) { }

  create(createAdoptionDto: CreateAdoptionDto): Observable<void> {
    return this.http.post<void>(`${environment.api_endpoint}/adoption`, createAdoptionDto);
  }
}
