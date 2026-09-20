import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { FarmAnimalSale } from '../../models/domain/farmAnimalSale';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleReadService {

    constructor(private http: HttpClient) { }

    findAll(): Promise<FarmAnimalSale[]> {
        return firstValueFrom(
            this.http.get<FarmAnimalSale[]>(`${environment.api_endpoint}/farm-animal-sale`)
        );
    }
}