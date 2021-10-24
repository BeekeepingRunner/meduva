import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {CalendarModule, DateAdapter} from "angular-calendar";
import {WorkerScheduleComponent} from "./component/worker/worker-schedule/worker-schedule.component";
import {adapterFactory} from "angular-calendar/date-adapters/date-fns";
import {DemoUtilsModule} from "./demo-utils/module";
import { PickWorkerComponent } from './component/worker/pick-worker/pick-worker.component';
import {MatTableModule} from "@angular/material/table";
import {RouterModule} from "@angular/router";
import { DayDialogComponent } from './component/dialog/day-dialog/day-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    WorkerScheduleComponent,
    PickWorkerComponent,
    DayDialogComponent,
  ],
  imports: [
    CommonModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
    DemoUtilsModule,
    MatTableModule,
    RouterModule,
    MatDialogModule,
  ]
})
export class ScheduleModule { }
