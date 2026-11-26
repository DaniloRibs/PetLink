import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthenticationService } from "../authentication";
import { CurrentUserService } from "../current-user";

export const authenticationGuard: CanActivateFn = (route, state) => {

    const router = inject(Router);
    const authenticationService = inject(AuthenticationService);

    const isAuthenticated = authenticationService.isAuthenticated();

    if (isAuthenticated) {
        return true;
    }

    inject(CurrentUserService).clear();
    router.navigate(['/account/sign-in']);
    return false;
};
