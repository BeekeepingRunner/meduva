import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {CalendarModule, DateAdapter} from "angular-calendar";
import {WorkerScheduleComponent} from "./component/worker/worker-schedule/worker-schedule.component";
import {adapterFactory} from "angular-calendar/date-adapters/date-fns";
import {DemoUtilsModule} from "./demo-utils/module";
import { PickWorkerComponent } from './component/worker/pick-worker/pick-worker.component';
import {MatTableModule} from "@angular/material/table";
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [
    WorkerScheduleComponent,
    PickWorkerComponent,
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
  ]
})
export class ScheduleModule { }
