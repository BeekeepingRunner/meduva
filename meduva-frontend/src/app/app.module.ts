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
import { ServicesComponent } from './component/services/services.component';
import { NewServiceComponent } from './component/services/new-service/new-service.component';
import {CurrencyPipe} from "@angular/common";
import { ServiceDetailsComponent } from './component/services/service-details/service-details.component';

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
    ServicesComponent,
    NewServiceComponent,
    ServiceDetailsComponent,
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
