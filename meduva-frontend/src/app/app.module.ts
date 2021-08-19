import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './component/profile/profile.component';
import { BoardAdminComponent } from './component/board-admin/board-admin.component';
import { BoardClientComponent } from './component/board-client/board-client.component';
import { BoardWorkerComponent } from './component/board-worker/board-worker.component';
import { BoardReceptionistComponent } from './component/board-receptionist/board-receptionist.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardClientComponent,
    BoardWorkerComponent,
    BoardReceptionistComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
