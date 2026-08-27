import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { VaccineCampaign } from '../../models/domain/campaign';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccineCampaignReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<VaccineCampaign[]> {
    return firstValueFrom(this.http.get<VaccineCampaign[]>(`${environment.api_endpoint}/campaigns`));
  }

  findByCompanyEmail(companyEmail: string): Promise<VaccineCampaign[]> {
    return firstValueFrom(
      this.http.get<VaccineCampaign[]>(`${environment.api_endpoint}/campaigns?companyEmail=${companyEmail}`)
    );
  }
}
