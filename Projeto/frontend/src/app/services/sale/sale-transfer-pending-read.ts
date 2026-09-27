import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { FarmAnimalSale } from '../../models/domain/farmAnimalSale';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class SaleTransferPendingReadService {

    constructor(private http: HttpClient) { }

    findPendingByBuyerId(buyerId: number): Promise<FarmAnimalSale[]> {
        return firstValueFrom(
            this.http.get<FarmAnimalSale[]>(`${environment.api_endpoint}/animal-sale/pending/${buyerId}`)
        );
    }
}