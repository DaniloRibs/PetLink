import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Announcement } from '../../models/domain/announcement';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class AnnouncementUpdateService {

    constructor(private http: HttpClient) { }

    update(announcement: Announcement): Observable<Announcement> {
        return this.http.put<Announcement>(`${environment.api_endpoint}/announcement/${announcement.id}`, announcement);
    }
}