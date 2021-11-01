import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {CalendarModule, DateAdapter} from "angular-calendar";
import {WorkerScheduleComponent} from "./component/worker/worker-schedule/worker-schedule.component";
import {adapterFactory} from "angular-calendar/date-adapters/date-fns";
import {DemoUtilsModule} from "./util/calendar-demo-utils/module";
import { PickWorkerComponent } from './component/worker/pick-worker/pick-worker.component';
import {MatTableModule} from "@angular/material/table";
import {RouterModule} from "@angular/router";
import { DayDialogComponent } from './component/dialog/day-dialog/day-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {NgxMaterialTimepickerModule} from "ngx-material-timepicker";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MyScheduleComponent } from './component/my-schedule/my-schedule.component';
import { RoomScheduleComponent } from './component/room/room-schedule/room-schedule.component';
import { PickRoomComponent } from './component/room/pick-room/pick-room.component';
import { PickItemComponent } from './component/item/pick-item/pick-item.component';
import { ItemScheduleComponent } from './component/item/item-schedule/item-schedule.component';
import {ItemDayDialogComponent} from "./component/dialog/item-day-dialog/item-day-dialog.component";
import {MatRadioModule} from "@angular/material/radio";
import {MatCheckboxModule} from "@angular/material/checkbox";

@NgModule({
  declarations: [
    WorkerScheduleComponent,
    PickWorkerComponent,
    DayDialogComponent,
    ItemDayDialogComponent,
    MyScheduleComponent,
    RoomScheduleComponent,
    PickRoomComponent,
    PickItemComponent,
    ItemScheduleComponent,
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
        MatButtonModule,
        NgxMaterialTimepickerModule,
        MatInputModule,
        ReactiveFormsModule,
        MatRadioModule,
        FormsModule,
        MatCheckboxModule,
    ]
})
export class ScheduleModule { }
