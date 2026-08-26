// import { HttpClient } from '@angular/common/http';
// import { Injectable } from '@angular/core';
// import { User } from '../../models/domain/user';
// import { firstValueFrom } from 'rxjs';
// import { environment } from '../../../environments/environment';

// @Injectable({
//   providedIn: 'root',
// })
// export class UserReadService {


//   constructor(private http: HttpClient) { }

//   findAll(): Promise<any> {
//     return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user`));
//   }

//   findById(id: string): Promise<User> {
//     return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user/${id}`));
//   }

//   findByEmail(email: string): Promise<User> {
//     return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user/email/${email}`));
//   }

// }
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/domain/user';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserReadService {

  constructor(private http: HttpClient) { }

  findAll(): Promise<any> {
    return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user`));
  }

  findById(id: string): Promise<User> {
    return firstValueFrom(this.http.get<any>(`${environment.api_endpoint}/user/${id}`));
  }

  // Antes: GET /user/email/{email} — essa rota so existiria em uma API de
  // verdade (com esse endpoint customizado). No json-server puro (usado em
  // dev, ver environment.development.ts) o jeito certo de filtrar e por
  // query param, que retorna um array.
  findByEmail(email: string): Promise<User[]> {
    return firstValueFrom(this.http.get<User[]>(`${environment.api_endpoint}/user?email=${email}`));
  }
}

