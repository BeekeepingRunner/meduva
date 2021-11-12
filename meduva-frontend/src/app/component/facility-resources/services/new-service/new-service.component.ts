import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {forbidValuesBetweenStep} from "../../../../util/validator/number-step";
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-new-service',
  templateUrl: './new-service.component.html',
  styleUrls: ['./new-service.component.css']
})
export class NewServiceComponent implements OnInit {

  form!: FormGroup;
  isSubmitted: boolean = false;
  didAddingFail: boolean = false;
  errorMessage: string = "";

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
        name: new FormControl('', [
          Validators.required
        ]),
        description: new FormControl(''),
        durationInMin: new FormControl('', [
          Validators.required,
          forbidValuesBetweenStep(15)
        ]),
        price: new FormControl('', [
          Validators.required
        ]),
        itemless: new FormControl(false),
      }
    );
  }

  roundPrice() {
    let price: number = this.form.controls.price.value;
    price = Math.round(price * 100) / 100;
    this.form.get('price')?.setValue(price);
  }

  addService() {
    let service: Service = {
      name: this.form.controls.name.value,
      description: this.form.controls.description.value,
      durationInMin: this.form.controls.durationInMin.value,
      price: this.form.controls.price.value,
      itemless: this.form.controls.itemless.value,
      deleted: false
    };

    this.servicesService.addNewService(service).subscribe(
      data => {
        this.didAddingFail = false;
        this.isSubmitted = true;
      },
      err => {
        this.didAddingFail = true;
        this.errorMessage = err.error.message;
        console.log(err)
      }
    )
  }
}
