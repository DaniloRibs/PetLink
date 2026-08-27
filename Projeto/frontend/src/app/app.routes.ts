// import { Routes } from '@angular/router';
// import { SignIn } from './views/account/sign-in/sign-in';
// import { SignUp } from './views/account/sign-up/sign-up';
// import { MyProfile } from './views/account/my-profile/my-profile';
// import { Help } from './views/pages/help/help';
// import { Home } from './views/pages/home/home';
// import { Main } from './views/pages/main/main';
// import { UserList } from './views/pages/user/user-list/user-list';
// import { UserEdit } from './views/pages/user/user-edit/user-edit';
// import { UserDetail } from './views/pages/user/user-detail/user-detail';
// import { NotFound } from './views/not-found/not-found';
// import { authenticationGuard } from './services/security/guard/athentication.guard';

// export const routes: Routes = [
//     {
//         path: 'account/sign-in',
//         component: SignIn
//     },
//     {
//         path: 'account/sign-up',
//         component: SignUp
//     },
//     {
//         path: '',
//         component: Main,
//         canActivate: [authenticationGuard],
//         children: [

//             {
//                 path: '',
//                 component: Home,
//             },

//             {
//                 path: 'account/my-profile',
//                 component: MyProfile
//             },
//             {
//                 path: 'help',
//                 component: Help
//             },
//             {
//                 path: 'user',
//                 children: [

//                     {
//                         path: 'list',
//                         component: UserList
//                     },
//                     {
//                         path: 'edit/:id',
//                         component: UserEdit
//                     },
//                     {
//                         path: 'detail/:id',
//                         component: UserDetail
//                     },

//                 ]
//             },
//             {
//                 path: '**',
//                 component: NotFound
//             }

//         ]






//     },



// ];

import { Routes } from '@angular/router';

import { Landing } from './views/public/landing/landing';
import { SignIn } from './views/account/sign-in/sign-in';
import { SignUp } from './views/account/sign-up/sign-up';

import { DashboardLayout } from './views/pages/dashboard/dashboard-layout/dashboard-layout';
import { DashboardHome } from './views/pages/dashboard/dashboard-home/dashboard-home';
import { PetCreate } from './views/pages/pet/pet-create/pet-create';

import { NotFound } from './views/not-found/not-found';
import { authenticationGuard } from './services/security/guard/athentication.guard';
import { AnnouncementListComponent } from './views/pages/announcement/announcement-list.component';

export const routes: Routes = [

    // Publicas
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

    // Area logada (PetLink)
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
                path: 'pets/novo',
                component: PetCreate,
            },
            {
                path: 'pages/announcement',
                component: AnnouncementListComponent
            },
        ],
    },

    {
        path: '**',
        component: NotFound,
    },
];

