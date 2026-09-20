import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateFarmAnimalDto } from '../../models/dto/create-farm-animal-dto';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class FarmAnimalCreateService {

    constructor(private http: HttpClient) { }

    create(farmAnimal: CreateFarmAnimalDto): Observable<void> {
        return this.http.post<void>(`${environment.api_endpoint}/animal`, farmAnimal);
    }
}