import { Component, OnInit } from '@angular/core';
import {Role, User} from "../../model/user";
import {TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  user!: User;
  error!: string;
  form!: FormGroup;
  errorMessage!: string;
  editFailed: boolean = false;
  editSuccessful: boolean = false;

  id!: number;


  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.populateFormWithUserData();
    this.buildForm();
  }

  private buildForm(){
    this.form = this.formBuilder.group({
      login: new FormControl( '', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(20),
        Validators.pattern('^[^-\\s]+$')

      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')
      ]),
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
    });
  }

  private populateFormWithUserData(){
    this.id = this.route.snapshot.params.id;
    this.userService.getUserDetails(this.id).subscribe(
      (data: User) => {
        this.user = data;
        this.form.patchValue({
          name: this.user.name,
          surname: this.user.surname,
          email: this.user.email,
          phoneNumber: this.user.phoneNumber,
          login: this.user.login
        })
      },
      err => {
        this.error = err.getError();
      });
  }

    onSubmit(){
    if(this.form.invalid){

      this.errorMessage = "Entered data must be correct";
      this.editFailed = true;
    }else{
      this.tryToSendUpdateRequest();
    }
  }

  private tryToSendUpdateRequest(){
    const login: string = this.form.controls.login.value;
    const name: string = this.form.controls.name.value;
    const surname: string = this.form.controls.surname.value;
    const phoneNumber: string = this.form.controls.phoneNumber.value;

    this.userService.editUser(login,name,surname,phoneNumber, this.id).subscribe(
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
}
