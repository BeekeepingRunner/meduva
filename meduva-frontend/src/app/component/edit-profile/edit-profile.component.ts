import { Component, OnInit } from '@angular/core';
import {Role, User} from "../../model/user";
import {TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  currentUser!: TokenUserInfo | null;
  userDetails!: User;
  userRole!: Role;
  error!: string;
  form!: FormGroup;


  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      login: new FormControl('', [
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
      }
    )
  }

}
