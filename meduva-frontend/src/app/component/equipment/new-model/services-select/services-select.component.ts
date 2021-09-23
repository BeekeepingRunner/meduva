import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-services-select',
  templateUrl: './services-select.component.html',
  styleUrls: ['./services-select.component.css']
})
export class ServicesSelectComponent implements OnInit {

  @Input() services!: Service[];
  @Output() selectedServicesEmmitter = new EventEmitter<Service[]>();
  selectedServices: Service[] = [];

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;

  constructor() { }

  ngOnInit(): void {
  }

  emitServices() {
    console.log(this.selectedServices);
    this.selectedServicesEmmitter.emit(this.selectedServices);
  }
}
