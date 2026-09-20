import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UpdateFarmAnimalDto } from '../../models/dto/update-farm-animal-dto';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class FarmAnimalUpdateService {

    constructor(private http: HttpClient) { }

    update(farmAnimal: UpdateFarmAnimalDto): Observable<void> {
        return this.http.put<void>(`${environment.api_endpoint}/animal/${farmAnimal.id}`, farmAnimal);
    }
}