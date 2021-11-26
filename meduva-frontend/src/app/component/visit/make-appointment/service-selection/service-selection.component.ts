import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {User} from "../../../../model/user";

@Component({
  selector: 'app-service-selection',
  templateUrl: './service-selection.component.html',
  styleUrls: ['./service-selection.component.css']
})
export class ServiceSelectionComponent implements OnInit {

  @Input()
  worker!: User | null;

  @Input()
  asRecepcionist = false;

  services: Service[] = [];

  @Output() reRenderSignalEmitter = new EventEmitter<boolean>();
  @Output() serviceEmitter = new EventEmitter<Service>();
  displayedColumns: string[] = ['name', 'duration', 'price'];

  constructor(
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    if (this.worker == null) {
      this.getAllServices();
    } else {
      this.getServicesForWorker();
    }
  }

  private getAllServices() {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      }, err => {
        console.log(err);
      }
    );
  }

  private getServicesForWorker() {
    if(this.worker?.id){
      this.servicesService.getAllPossibleWithWorker(this.worker.id).subscribe(
        services => {
          this.services = services;
        }, err => {
          console.log(err);
        }
      );
    }
  }

  onServiceSelect(service: Service) {
    this.reRenderSignalEmitter.emit(true);
    this.serviceEmitter.emit(service);
  }
}
