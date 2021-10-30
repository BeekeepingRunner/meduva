import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {forbidValuesBetweenStep} from "../../../../util/validator/number-step";
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {NewServiceComponent} from "../../../services/new-service/new-service.component";

@Component({
  selector: 'app-new-service-creator',
  templateUrl: './new-service-creator.component.html',
  styleUrls: ['./new-service-creator.component.css']
})
export class NewServiceCreatorComponent extends NewServiceComponent {


}
