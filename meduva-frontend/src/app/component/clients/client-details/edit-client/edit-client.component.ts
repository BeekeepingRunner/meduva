import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ClientService} from "../../../../service/client.service";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {Client} from "../../../../model/client";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";

@Component({
  selector: 'app-edit-client',
  templateUrl: '../../add-client/add-client.component.html',
  styleUrls: ['../../add-client/add-client.component.css']
})
export class EditClientComponent implements OnInit {

  heading: string = '';
  submitButtonText: string = 'Edit Client';

  form!: FormGroup;

  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  client!: Client;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private clientService: ClientService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.heading = 'Edit a Client';

    this.form = this.formBuilder.group({
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
        ]),
      }
    );

    let editedClientId: number = Number(this.route.snapshot.params.id);
    this.clientService.getClientById(editedClientId).subscribe(
      client => {
        this.client = client;
        this.form.patchValue({
          name: this.client.name,
          surname: this.client.surname,
          phoneNumber: this.client.phoneNumber
        });
      }
    );
  }

  onSubmit(){
    if (this.form.invalid) {
      this.errorMessage = "Entered data must be correct";
      this.isSignUpFailed = true;
    } else {
      this.client.name = this.form.get('name')?.value;
      this.client.surname = this.form.get('surname')?.value;
      this.client.phoneNumber = this.form.get('phoneNumber')?.value;

      this.clientService.editClient(this.client).subscribe(
        data => {
          this.openFeedbackDialog();
        }, err => {
          this.errorMessage = err.error.message;
        }
      );
    }
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Client edited successfully' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/client/list']);
      }
    );
  }
}
