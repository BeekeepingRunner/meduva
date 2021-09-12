import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

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
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
        name: new FormControl('', [Validators.required]),
        description: new FormControl(''),
        durationInMin: new FormControl(''),
        price: new FormControl(''),
      }
    );
  }

  addService() {

  }
}
