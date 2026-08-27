// import { inject } from "@angular/core";
// import { CanActivateFn, Router } from "@angular/router";
// import { AuthenticationService } from "../authentication";
// export const authenticationGuard: CanActivateFn = (route, state) => {

//     const router = inject(Router);
//     const authenticationService = inject(AuthenticationService);

//     const isAuthenticated = authenticationService.isAuthenticated();


//     if (isAuthenticated) {
//         return true;
//     }

//     if (router.url === 'account/sign-up') {
//         router.navigate(['account/sign-in']);
//         return false;
//     }

//     router.navigate(['account/sign-in']);
//     return false;
// };
import { inject, PLATFORM_ID } from "@angular/core";
import { isPlatformBrowser } from "@angular/common";
import { CanActivateFn, Router } from "@angular/router";
import { AuthenticationService } from "../authentication";

export const authenticationGuard: CanActivateFn = (route, state) => {

    const router = inject(Router);
    const authenticationService = inject(AuthenticationService);
    const platformId = inject(PLATFORM_ID);

    // No servidor (SSR) nao existe localStorage, entao isAuthenticated()
    // sempre voltaria false e mandaria pro login por engano, mesmo com o
    // usuario logado de verdade. Deixamos passar aqui; quem decide de
    // verdade e o guard rodando no navegador, depois da hidratacao.
    if (!isPlatformBrowser(platformId)) {
        return true;
    }

    const isAuthenticated = authenticationService.isAuthenticated();

    if (isAuthenticated) {
        return true;
    }

    router.navigate(['/account/sign-in']);
    return false;
};