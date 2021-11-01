import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {forbidValuesBetweenStep} from "../../../../util/validator/number-step";
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {NewServiceComponent} from "../../../services/new-service/new-service.component";
import {EquipmentItem} from "../../../../model/equipment";

@Component({
  selector: 'app-new-service-details-creator',
  templateUrl: './new-service-details-creator.component.html',
  styleUrls: ['./new-service-details-creator.component.css']
})
export class NewServiceDetailsCreatorComponent extends NewServiceComponent{
  @Output() newServiceEmitter = new EventEmitter<Service>();


  onServiceCreated() {
    let service: Service = {
      name: this.form.controls.name.value,
      description: this.form.controls.description.value,
      durationInMin: this.form.controls.durationInMin.value,
      price: this.form.controls.price.value,
      itemless: this.form.controls.itemless.value,
      deleted: false
    };
    this.newServiceEmitter.emit(service);
  }
}
