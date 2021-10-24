import {RoleGuardService as RoleGuard} from "./service/auth/role-guard.service";
import {Routes} from "@angular/router";
import {HomeComponent} from "./component/home/home.component";
import {LoginComponent} from "./component/login/login.component";
import {PasswordResetEmailInputComponent} from "./component/login-data/password-reset-email-input/password-reset-email-input.component";
import {PasswordResetComponent} from "./component/login-data/password-reset/password-reset.component";
import {RegisterComponent} from "./component/register/register.component";
import {ProfileComponent} from "./component/profile/profile.component";
import {UserListComponent} from "./component/user/user-list/user-list.component";
import {roleNames, UserRole} from "./model/user";
import {AccessDeniedComponent} from "./component/access-denied/access-denied.component";
import {ServiceListComponent} from "./component/services/service-list/service-list.component";
import {NewServiceComponent} from "./component/services/new-service/new-service.component";
import {EditProfileComponent} from "./component/profile/edit-profile/edit-profile.component";
import {ServiceDetailsComponent} from "./component/services/service-details/service-details.component";
import {RoomListComponent} from "./component/rooms/room-list/room-list.component";
import {NewRoomComponent} from "./component/rooms/new-room/new-room.component";
import {RoomDetailsComponent} from "./component/rooms/room-details/room-details.component";
import {EditEmailComponent} from "./component/profile/edit-email/edit-email.component";
import {ActivateNewEmailComponent} from "./component/login-data/activate-new-email/activate-new-email.component";
import {EquipmentListComponent} from "./component/equipment/equipment-list/equipment-list.component";
import {NewModelComponent} from "./component/equipment/new-model/new-model.component";
import {ModelDetailsComponent} from "./component/equipment/model-details/model-details.component";
import {ChooseServiceComponent} from "./component/visit/choose-service/choose-service.component";
import {PickTermComponent} from "./component/visit/pick-term/pick-term.component";
import {SpecificUserComponent} from "./component/specific-user-profile/specific-user.component";
import {EditRoleComponent} from "./component/specific-user-profile/edit-role/edit-role.component";
import {PickClientComponent} from "./component/visit/pick-client/pick-client.component";
import {SummaryComponent} from "./component/visit/summary/summary.component";
import {WorkerServicesComponent} from "./component/specific-user-profile/worker-services/worker-services.component";
import {EditPerformedServicesComponent} from "./component/rooms/edit-performed-services/edit-performed-services.component";
import {ChangePasswordComponent} from "./component/profile/change-password/change-password.component";
import {ClientListComponent} from "./component/clients/client-list/client-list.component";
import {ClientDetailsComponent} from "./component/clients/client-details/client-details.component";
import {AddClientComponent} from "./component/clients/add-client/add-client.component";
import {EditClientComponent} from "./component/clients/client-details/edit-client/edit-client.component";
import {WorkerScheduleComponent} from "./schedule/component/worker-schedule/worker-schedule.component";

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
    path: 'profile/change-password',
    component: ChangePasswordComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_CLIENT]
    }
  },

  {
    path: 'profile/edit-profile',
    component: EditProfileComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_CLIENT]
    }
  },
  {
    path: 'profile/edit-profile/:id',
    component: EditProfileComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'profile/edit-email',
    component: EditEmailComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_CLIENT]
    }
  },
  {
    path: 'profile/edit-email/:id',
    component: EditEmailComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'new-email-activation/:token',
    component: ActivateNewEmailComponent,
  },
  {
    path: 'client/list',
    component: ClientListComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  {
    path: 'client/details',
    component: ClientDetailsComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  {
    path: 'client/new',
    component: AddClientComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_RECEPTIONIST]
    }
  },
  {
    path: 'client/edit/:id',
    component: EditClientComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_RECEPTIONIST]
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
    path: 'specific-user/:id',
    component:SpecificUserComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_RECEPTIONIST]
    }
  },
  {
    path: 'specific-user/edit-role/:id',
    component: EditRoleComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'services',
    component: ServiceListComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'service/:id',
    component: ServiceDetailsComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'specific-user/worker-services/:id',
    component: WorkerServicesComponent,
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
  {
    path: 'rooms',
    component: RoomListComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'rooms/add-room',
    component: NewRoomComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'room/:id',
    component: RoomDetailsComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'room/:id/edit-performed-services',
    component: EditPerformedServicesComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'equipment',
    component: EquipmentListComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'equipment/model/:id',
    component: ModelDetailsComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'equipment/add-model',
    component: NewModelComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_ADMIN]
    }
  },
  {
    path: 'visit/pick-service',
    component: ChooseServiceComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_CLIENT]
    }
  },
  {
    path: 'visit/pick-term',
    component: PickTermComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  {
    path: 'visit/pick-client',
    component: PickClientComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  {
    path: 'visit/summary',
    component: SummaryComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  {
    path: 'schedule/my',
    component: WorkerScheduleComponent,
    canActivate: [RoleGuard],
    data: {
      expectedRole: roleNames[UserRole.ROLE_WORKER]
    }
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];
