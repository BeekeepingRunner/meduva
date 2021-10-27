import { Component, OnInit } from '@angular/core';
import {User} from "../../../model/user";
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserService} from "../../../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-worker-services',
  templateUrl: './worker-services.component.html',
  styleUrls: ['./worker-services.component.css']
})
export class WorkerServicesComponent implements OnInit {

  user!: User;
  isAWorker: boolean = false;
  error!: string;
  form!: FormGroup;
  errorMessage!: string;
  editFailed: boolean = false;
  editSuccessful: boolean = false;
  services: Service[] = [];
  servicesId: number[] = [];
  selection: SelectionModel<Service>;

  displayedColumns: string[] = ['name', 'select'];


  userId!: number;


  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private servicesService: ServicesService,
              private route: ActivatedRoute) {
    const initialSelection: Service[] | undefined = [];
    const allowMultiSelect = true;
    this.selection = new SelectionModel<Service>(allowMultiSelect, initialSelection);
  }

  ngOnInit(): void {
    this.populateFormWithUserData();
    this.buildForm();
    this.getAllServices();
    this.getWorkerServices(this.userId);

  }

  private buildForm(){

    this.form = this.formBuilder.group({
      checkControl: false
    });
  }

  private populateFormWithUserData(){
    this.userId = this.route.snapshot.params.id;
    this.userService.getUserDetails(this.userId).subscribe(
      (data: User) => {
        this.user = data;
        this.isUserAWorker();
      },
      err => {
        this.error = err.getError();
      });

  }
  getAllServices() {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      })
  }

  getWorkerServices(userId: number){
    this.userService.getWorkerServices(userId).subscribe(

      services => {

        /** Fills the array which contain worker services id, which are saved in database */
        services.forEach(serv => {
          if(serv.id)
          this.servicesId.push(serv.id);
        })

          /** Selects all rows that are services containing the identifier from servicesId array */
        this.services.forEach(row => {
          if(row.id)
          if(this.servicesId.includes(row.id))
          this.selection.select(row)
        });
      }
    );
}

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.services.length;
    return numSelected == numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.services.forEach(row => this.selection.select(row));
  }

    onSubmit(){
    if(this.form.invalid){

      this.errorMessage = "Form data is invalid";
      this.editFailed = true;
    }
    else{
      if(this.selection.isEmpty()){

        this.errorMessage = "Any service must be selected";
        this.editFailed = true;
      }
      else{
        this.tryToSendUpdateRequest();
      }
    }
  }

  private tryToSendUpdateRequest(){
    let selectedServices: number[] = [];
    for(let serv of this.selection.selected){
      if(serv.id)
      selectedServices.push((serv.id));
    }

    this.userService.assignServicesToWorker(selectedServices, this.userId).subscribe(
      data => {
        this.editFailed = false;
        this.editSuccessful = true;

      },
      err => {
        this.errorMessage = err.error.message;
        this.editFailed = true;
      }
    )

  }

  isUserAWorker(){
    let role = this.userService.getMasterRole(this.user.roles);
    if(role.name != 'ROLE_CLIENT'){
      this.isAWorker = true;
    }else {
      this.isAWorker = false;
    }
  }


}
