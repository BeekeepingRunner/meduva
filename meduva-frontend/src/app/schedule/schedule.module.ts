import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {CalendarModule, DateAdapter} from "angular-calendar";
import {WorkerScheduleComponent} from "./worker-schedule/worker-schedule.component";
import {adapterFactory} from "angular-calendar/date-adapters/date-fns";
import {DemoUtilsModule} from "../util/demo-utils/module";



@NgModule({
  declarations: [
    WorkerScheduleComponent
  ],
  imports: [
    CommonModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
    DemoUtilsModule,
  ]
})
export class ScheduleModule { }
