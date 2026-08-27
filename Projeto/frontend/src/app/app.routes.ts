import { Routes } from '@angular/router';

import { Landing } from './views/public/landing/landing';
import { SignIn } from './views/account/sign-in/sign-in';
import { SignUp } from './views/account/sign-up/sign-up';
import { MyProfile } from './views/account/my-profile/my-profile';

import { DashboardLayout } from './views/pages/dashboard/dashboard-layout/dashboard-layout';
import { DashboardHome } from './views/pages/dashboard/dashboard-home/dashboard-home';
import { PetCreate } from './views/pages/pet/pet-create/pet-create';
import { PetList } from './views/pages/pet/pet-list/pet-list';
import { PetDetail } from './views/pages/pet/pet-detail/pet-detail';
import { AnnouncementList } from './views/pages/announcement/announcement-list/announcement-list';
import { AdoptionHub } from './views/pages/adoption/adoption-hub/adoption-hub';

import { NotFound } from './views/not-found/not-found';
import { authenticationGuard } from './services/security/guard/authentication.guard';

export const routes: Routes = [

    {
        path: '',
        component: Landing,
    },
    {
        path: 'account/sign-in',
        component: SignIn,
    },
    {
        path: 'account/sign-up',
        component: SignUp,
    },
    {
        path: 'painel',
        component: DashboardLayout,
        canActivate: [authenticationGuard],
        children: [
            {
                path: '',
                component: DashboardHome,
            },
            {
                path: 'pets',
                component: PetList,
            },
            {
                path: 'pets/novo',
                component: PetCreate,
            },
            {
                path: 'pets/:id',
                component: PetDetail,
            },
            {
                path: 'anuncios',
                component: AnnouncementList,
            },
            {
                path: 'conta/perfil',
                component: MyProfile,
            },
            {
                path: 'adocoes',
                component: AdoptionHub,
            },
        ],
    },

    {
        path: '**',
        component: NotFound,
    },
];
