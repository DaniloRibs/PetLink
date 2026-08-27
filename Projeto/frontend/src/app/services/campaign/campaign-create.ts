import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VaccineCampaign } from '../../models/domain/campaign';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccineCampaignCreateService {

  constructor(private http: HttpClient) { }

  create(campaign: VaccineCampaign): Observable<VaccineCampaign> {
    return this.http.post<VaccineCampaign>(`${environment.api_endpoint}/campaigns`, campaign);
  }
}
