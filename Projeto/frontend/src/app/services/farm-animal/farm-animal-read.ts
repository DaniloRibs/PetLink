import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Pet } from '../../models/domain/pet';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class FarmAnimalReadService {

    constructor(private http: HttpClient) { }

    findByOwnerId(ownerId: number): Promise<Pet[]> {
        return firstValueFrom(
            this.http.get<Pet[]>(`${environment.api_endpoint}/user/${ownerId}/animal`)
        );
    }

    findById(id: string): Promise<Pet> {
        return firstValueFrom(this.http.get<Pet>(`${environment.api_endpoint}/animal/${id}`));
    }
}