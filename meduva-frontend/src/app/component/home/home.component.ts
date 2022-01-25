import { Component, OnInit } from '@angular/core';
import {UserService} from "../../service/user.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {JwtStorageService} from "../../service/token/jwt-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  form!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private tokenStorage: JwtStorageService,
    private router: Router
  ) {
  }

  ngOnInit(): void {

    if (this.hasJustLoggedIn()) {
      this.router.navigate(['/profile']);
    }

    this.form = this.formBuilder.group({
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
        phone: new FormControl('', [
          Validators.pattern('^(\\+[0-9]{1,4})?[0-9]{6,12}$')
        ]),
      message: new FormControl('', [
        Validators.required
      ])
      }
    );
  }

  hasJustLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

}
