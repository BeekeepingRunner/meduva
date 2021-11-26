import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MatSidenavModule} from "@angular/material/sidenav";

import { AppComponent } from './app.component';
import { LoginComponent } from './component/site-entry/login/login.component';
import { RegisterComponent } from './component/site-entry/register/register.component';
import { HomeComponent } from './component/home/home.component';
import { ProfileComponent } from './component/user/profile/profile.component';
import { SpecificUserComponent} from "./component/user/specific-user-profile/specific-user.component";
import { EditProfileComponent } from './component/user/profile/edit-profile/edit-profile.component';
import { EditEmailComponent } from './component/user/profile/edit-email/edit-email.component';
import { ActivateNewEmailComponent } from './component/site-entry/login-data/activate-new-email/activate-new-email.component';

import { authInterceptorProviders } from './helper/auth.interceptor';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatDividerModule} from "@angular/material/divider";
import {MatButtonModule} from "@angular/material/button";
import {MatTreeModule} from "@angular/material/tree";
import {MatExpansionModule} from "@angular/material/expansion";
import { PasswordResetEmailInputComponent } from './component/site-entry/login-data/password-reset-email-input/password-reset-email-input.component';
import { PasswordResetComponent } from './component/site-entry/login-data/password-reset/password-reset.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { UserListComponent } from './component/user/user-list/user-list.component';
import {MatTableModule} from "@angular/material/table";

import {routes} from "./app.routes";
import { AccessDeniedComponent } from './component/access-denied/access-denied.component';
import { ServiceListComponent } from './component/facility-resources/services/service-list/service-list.component';
import { NewServiceComponent } from './component/facility-resources/services/new-service/new-service.component';
import {CurrencyPipe, DatePipe} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatListModule} from "@angular/material/list";
import { ServiceDetailsComponent } from './component/facility-resources/services/service-details/service-details.component';
import { ConfirmationDialogComponent } from './component/dialog/confirmation-dialog/confirmation-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { RoomListComponent } from './component/facility-resources/rooms/room-list/room-list.component';
import { NewRoomComponent } from './component/facility-resources/rooms/new-room/new-room.component';
import { RoomDetailsComponent } from './component/facility-resources/rooms/room-details/room-details.component';
import { EquipmentListComponent } from './component/facility-resources/equipment/equipment-list/equipment-list.component';
import { NewModelComponent } from './component/facility-resources/equipment/new-model/new-model.component';
import {MatStepperModule} from "@angular/material/stepper";
import { RoomSelectComponent } from './component/facility-resources/equipment/new-model/room-select/room-select.component';
import { RoomSelectionDialogComponent } from './component/dialog/room-selection-dialog/room-selection-dialog.component';
import { ServicesSelectComponent } from './component/facility-resources/equipment/new-model/services-select/services-select.component';
import { ModelFormComponent } from './component/facility-resources/equipment/new-model/model-form/model-form.component';
import { ModelDetailsComponent } from './component/facility-resources/equipment/model-details/model-details.component';
import { FeedbackDialogComponent } from './component/dialog/feedback-dialog/feedback-dialog.component';
import { ChooseServiceComponent } from './component/visit/choose-service/choose-service.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {EditRoleComponent} from "./component/user/specific-user-profile/edit-role/edit-role.component";
import {MatSelectModule} from "@angular/material/select";
import {WorkerServicesComponent} from "./component/user/specific-user-profile/worker-services/worker-services.component";
import { EditPerformedServicesComponent } from './component/facility-resources/rooms/edit-performed-services/edit-performed-services.component';
import { ChangePasswordComponent } from './component/user/profile/change-password/change-password.component';
import {CreatorComponent} from "./component/creator/creator.component";
import {NumberOfRoomsComponent} from "./component/creator/assign-rooms/number-of-rooms/number-of-rooms.component";
import {ConfigureRoomsCreatorComponent} from "./component/creator/assign-rooms/configure-rooms-creator/configure-rooms-creator.component";
import {ConfigureRoomsCreatorDialogComponent} from "./component/dialog/configure-rooms-creator-dialog/configure-rooms-creator-dialog.component";
import {EquipmentListCreatorComponent} from "./component/creator/assign-equipment/equipment-list-creator/equipment-list-creator.component";
import {NewModelCreatorComponent} from "./component/creator/assign-equipment/new-model-creator/new-model-creator.component";
import {ConfigureEquipmentCreatorDialogComponent} from "./component/dialog/configure-equipment-creator-dialog/configure-equipment-creator-dialog.component";
import {ServiceListCreatorComponent} from "./component/creator/assign-services/service-list-creator/service-list-creator.component";
import {NewServiceCreatorComponent} from "./component/creator/assign-services/new-service-creator/new-service-creator.component";
import {ConfigureServicesCreatorDialogComponent} from "./component/dialog/configure-services-creator-dialog/configure-services-creator-dialog.component";
import {NewServiceDetailsCreatorComponent} from "./component/creator/assign-services/new-service-details-creator/new-service-details-creator.component";
import {ItemsSelectServiceCreatorComponent} from "./component/creator/assign-services/items-select-service-creator/items-select-service-creator.component";
import {RoomsSelectServiceCreatorComponent} from "./component/creator/assign-services/rooms-select-service-creator/rooms-select-service-creator.component";
import { ClientListComponent } from './component/clients/client-list/client-list.component';
import { ClientDetailsComponent } from './component/clients/client-details/client-details.component';
import { AddClientComponent } from './component/clients/add-client/add-client.component';
import { EditClientComponent } from './component/clients/client-details/edit-client/edit-client.component';
import {ScheduleModule} from "./schedule/schedule.module";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import { VisitHistoryComponent } from './component/visit/visit-history/visit-history.component';
import { MakeAppointmentComponent } from './component/visit/make-appointment/make-appointment.component';
import { PlanYourVisitComponent } from './component/visit/plan-your-visit/plan-your-visit.component';
import { ServiceSelectionComponent } from './component/visit/make-appointment/service-selection/service-selection.component';
import { WorkerSelectionComponent } from './component/visit/make-appointment/worker-selection/worker-selection.component';
import {
  AsyncDatePickerHeader,
  TermSelectionComponent
} from './component/visit/make-appointment/term-selection/term-selection.component';
import { VisitSummaryComponent } from './component/visit/make-appointment/visit-summary/visit-summary.component';
import { ClientSelectionComponent } from './component/visit/plan-your-visit/client-selection/client-selection.component';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from "@angular/material/snack-bar";
import { ClientVisitsComponent } from './component/clients/client-details/client-visits/client-visits.component';
import {ConfirmationWithWarningDialogComponent} from "./component/dialog/confirmation-with-warning-dialog/confirmation-with-warning-dialog.component";
import { VisitDetailsComponent } from './component/visit/visit-details/visit-details.component';
<<<<<<< HEAD
import {ChooseOptionDialogComponent} from "./component/dialog/choose-option-dialog/choose-option-dialog.component";
=======
import { EditVisitTermComponent } from './component/visit/edit-visit-term/edit-visit-term.component';
>>>>>>> c3a783754ab8c495e69971e8a8b97366e18fda81



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    PasswordResetEmailInputComponent,
    PasswordResetComponent,
    EditEmailComponent,
    ActivateNewEmailComponent,
    UserListComponent,
    SpecificUserComponent,
    AccessDeniedComponent,
    ServiceListComponent,
    NewServiceComponent,
    EditProfileComponent,
    ServiceDetailsComponent,
    ConfirmationDialogComponent,
    RoomListComponent,
    NewRoomComponent,
    RoomDetailsComponent,
    EquipmentListComponent,
    NewModelComponent,
    RoomSelectComponent,
    RoomSelectionDialogComponent,
    ServicesSelectComponent,
    ModelFormComponent,
    ModelDetailsComponent,
    FeedbackDialogComponent,
    ChooseServiceComponent,
    EditRoleComponent,
    WorkerServicesComponent,
    EditPerformedServicesComponent,
    ChangePasswordComponent,

    CreatorComponent,
    NumberOfRoomsComponent,
    ConfigureRoomsCreatorComponent,
    ConfigureRoomsCreatorDialogComponent,
    EquipmentListCreatorComponent,
    NewModelCreatorComponent,
    ConfigureEquipmentCreatorDialogComponent,
    ServiceListCreatorComponent,
    NewServiceCreatorComponent,
    NewServiceDetailsCreatorComponent,
    ConfigureServicesCreatorDialogComponent,
    ItemsSelectServiceCreatorComponent,
    RoomsSelectServiceCreatorComponent,

    ClientListComponent,
    ClientDetailsComponent,
    AddClientComponent,
    EditClientComponent,
    AsyncDatePickerHeader,
    VisitHistoryComponent,
    MakeAppointmentComponent,
    PlanYourVisitComponent,
    ServiceSelectionComponent,
    WorkerSelectionComponent,
    TermSelectionComponent,
    VisitSummaryComponent,
    ClientSelectionComponent,
    ClientVisitsComponent,

    ConfirmationWithWarningDialogComponent,
    VisitDetailsComponent,
    ChooseOptionDialogComponent,
    EditVisitTermComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(routes),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatToolbarModule,
    MatIconModule,
    MatDividerModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatButtonModule,
    MatTreeModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatCardModule,
    MatListModule,
    MatDialogModule,
    MatStepperModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    ScheduleModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule
  ],
  exports: [
    RouterModule
  ],
  providers: [
    authInterceptorProviders,
    CurrencyPipe,
    DatePipe,
    MatDatepickerModule,
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 2500}},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
