import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Announcement } from '../../models/domain/announcement';
import { environment } from '../../../environments/environment';
import { CreateAnnouncementDto } from '../../models/dto/create-announcement-dto';

@Injectable({
  providedIn: 'root',
})
export class AnnouncementCreateService {

  constructor(private http: HttpClient) { }

  create(announcement: CreateAnnouncementDto): Observable<Announcement> {
    return this.http.post<Announcement>(`${environment.api_endpoint}/announcement`, announcement);
  }
}
