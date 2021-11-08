import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ClientService} from "../../../../service/client.service";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {Client} from "../../../../model/client";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";
import {controlsConfig} from "../../add-client/add-client.component";

@Component({
  selector: 'app-edit-client',
  templateUrl: '../../add-client/add-client.component.html',
  styleUrls: ['../../add-client/add-client.component.css']
})
export class EditClientComponent implements OnInit {

  heading: string = 'Edit a Client';
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
    this.form = this.formBuilder.group(controlsConfig);

    let editedClientId: number = Number(this.route.snapshot.params.id);
    this.fillFormWithClientData(editedClientId);
  }

  private fillFormWithClientData(editedClientId: number) {
    this.clientService.getClientById(editedClientId).subscribe(
      client => {
        this.client = client;
        this.form.patchValue({
          name: this.client.name,
          surname: this.client.surname,
          phoneNumber: this.client.phoneNumber
        });
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(){
    if (this.form.invalid) {
      this.errorMessage = "Entered data must be correct";
      this.isSignUpFailed = true;
    } else {
      this.setNewClientData();
      this.sendEditRequest();
    }
  }

  private setNewClientData() {
    this.client.name = this.form.get('name')?.value;
    this.client.surname = this.form.get('surname')?.value;
    this.client.phoneNumber = this.form.get('phoneNumber')?.value;
  }

  private sendEditRequest() {
    this.clientService.editClient(this.client).subscribe(
      success => {
        this.openFeedbackDialog();
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
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
