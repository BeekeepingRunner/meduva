import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MatSidenavModule} from "@angular/material/sidenav";

import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { HomeComponent } from './component/home/home.component';
import { ProfileComponent } from './component/profile/profile.component';
import { BoardAdminComponent } from './component/board-admin/board-admin.component';
import { EditProfileComponent } from './component/edit-profile/edit-profile.component';

import { authInterceptorProviders } from './helper/auth.interceptor';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatDividerModule} from "@angular/material/divider";
import {MatButtonModule} from "@angular/material/button";
import {MatTreeModule} from "@angular/material/tree";
import {MatExpansionModule} from "@angular/material/expansion";
import { PasswordResetEmailInputComponent } from './component/password-reset-email-input/password-reset-email-input.component';
import { PasswordResetComponent } from './component/password-reset/password-reset.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { UserListComponent } from './component/user-list/user-list.component';
import {MatTableModule} from "@angular/material/table";
import {routes} from "./app.routes";
import { AccessDeniedComponent } from './component/access-denied/access-denied.component';
import { ServiceListComponent } from './component/services/service-list/service-list.component';
import { NewServiceComponent } from './component/services/new-service/new-service.component';
import {CurrencyPipe} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatListModule} from "@angular/material/list";
import { ServiceDetailsComponent } from './component/services/service-details/service-details.component';
import { ConfirmationDialogComponent } from './component/dialog/confirmation-dialog/confirmation-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { RoomListComponent } from './component/rooms/room-list/room-list.component';
import { NewRoomComponent } from './component/rooms/new-room/new-room.component';
import { RoomDetailsComponent } from './component/rooms/room-details/room-details.component';
import { EquipmentListComponent } from './component/equipment/equipment-list/equipment-list.component';
import { NewModelComponent } from './component/equipment/new-model/new-model.component';
import {MatStepperModule} from "@angular/material/stepper";
import { RoomSelectComponent } from './component/equipment/new-model/room-select/room-select.component';
import { RoomSelectionDialogComponent } from './component/dialog/room-selection-dialog/room-selection-dialog.component';
import { ServicesSelectComponent } from './component/equipment/new-model/services-select/services-select.component';
import { ModelFormComponent } from './component/equipment/new-model/model-form/model-form.component';
import { ModelDetailsComponent } from './component/equipment/model-details/model-details.component';
import { FeedbackDialogComponent } from './component/dialog/feedback-dialog/feedback-dialog.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    BoardAdminComponent,
    PasswordResetEmailInputComponent,
    PasswordResetComponent,
    UserListComponent,
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
    CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory }),
  ],
  exports: [
    RouterModule
  ],
  providers: [
    authInterceptorProviders,
    CurrencyPipe,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
