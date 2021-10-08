import { Component, OnInit } from '@angular/core';
import {Role, User, UserRole} from "../../model/user";
import {TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-edit-role',
  templateUrl: './edit-role.component.html',
  styleUrls: ['./edit-role.component.css']
})
export class EditRoleComponent implements OnInit {

  user!: User;
  error!: string;
  form!: FormGroup;
  errorMessage!: string;
  editFailed: boolean = false;
  editSuccessful: boolean = false;
  roles: Role[] = [];


id!: number;


  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.populateFormWithUserData();
    this.buildForm();
    this.createArrayforSelect();

  }

  private buildForm(){
    this.form = this.formBuilder.group({
      role: new FormControl('', [
        Validators.required,
      ]),
    });
  }

  private populateFormWithUserData(){
    this.id = this.route.snapshot.params.id;
    this.userService.getUserDetails(this.id).subscribe(
      (data: User) => {
        this.user = data;
      },
      err => {
        this.error = err.getError();
      });
  }

    onSubmit(){
    if(this.form.invalid){

      this.errorMessage = "Role must be selected";
      this.editFailed = true;
    }else{
      this.tryToSendUpdateRequest();
    }
  }

  private tryToSendUpdateRequest(){
    const roleId: number = this.form.controls.role.value;

    this.userService.editRole(roleId, this.id).subscribe(
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

  private createArrayforSelect(){

    for (const [propertyKey, propertyValue] of Object.entries(UserRole)) {
      if (!Number.isNaN(Number(propertyKey))) {
        continue;
      }
      this.roles.push({ id: Number(propertyValue)+1, name: propertyKey });
    }
}
}
