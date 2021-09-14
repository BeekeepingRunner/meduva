import {AuthGuardService as AuthGuard} from "./service/auth/auth-guard.service";
import {RoleGuardService as RoleGuard} from "./service/auth/role-guard.service";
import {Routes} from "@angular/router";
import {HomeComponent} from "./component/home/home.component";
import {LoginComponent} from "./component/login/login.component";
import {PasswordResetEmailInputComponent} from "./component/password-reset-email-input/password-reset-email-input.component";
import {PasswordResetComponent} from "./component/password-reset/password-reset.component";
import {RegisterComponent} from "./component/register/register.component";
import {ProfileComponent} from "./component/profile/profile.component";
import {BoardAdminComponent} from "./component/board-admin/board-admin.component";
import {UserListComponent} from "./component/user-list/user-list.component";
import {roleNames, UserRole} from "./model/user";
import {AccessDeniedComponent} from "./component/access-denied/access-denied.component";
import {ServicesComponent} from "./component/services/services.component";
import {NewServiceComponent} from "./component/services/new-service/new-service.component";

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'login/reset-password-email', component: PasswordResetEmailInputComponent },
  { path: 'login/password-reset/:resetToken', component: PasswordResetComponent },
  { path: 'access-denied', component: AccessDeniedComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_CLIENT]
    }
  },
  {
    path: 'admin',
    component: BoardAdminComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'users',
    component: UserListComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'services',
    component: ServicesComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'services/add-service',
    component: NewServiceComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];
