import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VaccinationCardDownloadService {

  constructor(private http: HttpClient) { }

  /**
   * Baixa o PDF da carteira de vacinação. Pets usam /pet/{id}/vaccination-card e
   * animais de fazenda usam /animal/{id}/vaccination-card.
   */
  download(animalId: number, isFarm: boolean): Observable<HttpResponse<Blob>> {
    const resource = isFarm ? 'animal' : 'pet';

    return this.http.get(`${environment.api_endpoint}/${resource}/${animalId}/vaccination-card`, {
      responseType: 'blob',
      observe: 'response',
    });
  }
}
