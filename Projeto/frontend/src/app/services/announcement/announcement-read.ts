import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Announcement } from '../../models/domain/announcement';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AnnouncementReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<Announcement[]> {
    return firstValueFrom(this.http.get<Announcement[]>(`${environment.api_endpoint}/announcements`));
  }

  findByCompanyEmail(companyEmail: string): Promise<Announcement[]> {
    return firstValueFrom(
      this.http.get<Announcement[]>(`${environment.api_endpoint}/announcements?companyEmail=${companyEmail}`)
    );
  }
}
