import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.css']
})
export class ServicesComponent implements OnInit {

  services: any;
  displayedColumns: string[] = ['name', 'duration', 'price'];

  constructor() { }

  ngOnInit(): void {
  }

}
