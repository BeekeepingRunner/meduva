import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {Router} from "@angular/router";
import {ClientService} from "../../../service/client.service";

@Component({
  selector: 'app-add-client',
  templateUrl: './add-client.component.html',
  styleUrls: ['./add-client.component.css']
})
export class AddClientComponent implements OnInit {

  heading: string = '';
  submitButtonText: string = 'Add Client';

  form!: FormGroup;

  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private clientService: ClientService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.heading = "Add new Client";
    this.form = this.formBuilder.group(controlsConfig);
  }

  onSubmit(){
    if(this.form.invalid){
      this.errorMessage = "Entered data must be correct";
      this.isSignUpFailed = true;
    }else{
      this.sendNewClient();
    }
  }

  private sendNewClient() {
    let newClient = {
      name : this.form.controls.name.value,
      surname : this.form.controls.surname.value,
      phoneNumber : this.form.controls.phoneNumber.value
    };
    console.log(newClient);
    this.clientService.addClient(newClient).subscribe(
      data => {
        this.openFeedbackDialog();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'New Client added Successfully.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/client/list']);
      }
    );
  }
}

export const controlsConfig = {
  name: new FormControl('', [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(30),
    Validators.pattern('^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.\'-]+$'),
    Validators.pattern('^[^-\\s]+$')
  ]),
    surname: new FormControl('', [
  Validators.required,
  Validators.minLength(1),
  Validators.maxLength(30),
  Validators.pattern('^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.\'-]+$'),
  Validators.pattern('^[^-\\s]+$')

]),
  phoneNumber: new FormControl('', [
  Validators.required,
  Validators.pattern('^(\\+[0-9]{1,4})?[0-9]{6,12}$')
])
};
