// import { HttpClient, HttpHeaders } from '@angular/common/http';
// import { Injectable } from '@angular/core';
// import { UserCredentialDto } from '../../models/dto/user-credential-dto';
// import { Observable } from 'rxjs';
// import { environment } from '../../../environments/environment';

// @Injectable({
//   providedIn: 'root',
// })
// export class AuthenticationService {
//   constructor(private http: HttpClient) {

//   }

//   authenticate(credentials: UserCredentialDto): Observable<any> {
//     console.log('Preparando para authenticar o usuario..');

//     const headers = new HttpHeaders({ 'Content-Type': 'aplication/json' });

//     const body = {
//     }

//     //Funciona somente no Json Server
//     //comentar depois
//     return this.http.get<any>(`${environment.authentication_api_endpoint}/user/1`);

//   }

//   isAuthenticated() {
//     if (typeof window === 'undefined') {
//       return false;
//     }


//     let email = localStorage.getItem('email');
//     if (email != null) {
//       console.log('email encontrado, realizando o login...');
//       return true;
//     }
//     return false;
//   }

//   logout() {
//     localStorage.clear();
//   }

//   addDataToLocalStorage(email: string) {
//     console.log('adicionando dados no navegador');

//     localStorage.setItem('email', email);

//   }

//   getAuthenticatedUserEmail() {
//     let email = localStorage.getItem('email');
//     if (email == null) {
//       throw new Error('Email nao encotnrado no cache')
//     }
//     return email;
//   }


// }

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserCredentialDto } from '../../models/dto/user-credential-dto';
import { from, map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../../models/domain/user';
import { UserReadService } from '../user/user-read';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private http: HttpClient,
    private userReadService: UserReadService,
  ) { }

  // Antes: fazia sempre um GET fixo em /user/1, ignorando o que o usuario
  // digitava (so funcionava "por acaso"). Agora busca o usuario pelo email
  // no json-server e confere a senha.
  authenticate(credentials: UserCredentialDto): Observable<User> {
    console.log('Preparando para autenticar o usuario...');

    return from(this.userReadService.findByEmail(credentials.email)).pipe(
      map((users: any) => {
        const usersFound: User[] = Array.isArray(users) ? users : (users ? [users] : []);
        const user = usersFound.find(u => u.password === credentials.password);

        if (!user) {
          throw new Error('Email e/ou senha incorretos');
        }

        return user;
      })
    );
  }

  isAuthenticated() {
    if (typeof window === 'undefined') {
      return false;
    }

    let email = localStorage.getItem('email');
    if (email != null) {
      return true;
    }
    return false;
  }

  logout() {
    localStorage.clear();
  }

  addDataToLocalStorage(email: string) {
    localStorage.setItem('email', email);
  }

  getAuthenticatedUserEmail() {
    let email = localStorage.getItem('email');
    if (email == null) {
      throw new Error('Email nao encontrado no cache');
    }
    return email;
  }
}

