import { Component, OnInit } from '@angular/core';
import {Client} from "../../../model/client";
import {VisitService} from "../../../service/visit.service";
import {UserService} from "../../../service/user.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {Router} from "@angular/router";
import {User} from "../../../model/user";
import {Service} from "../../../model/service";
import {WorkerService} from "../../../service/worker.service";

@Component({
  selector: 'app-pick-worker',
  templateUrl: './select-worker.component.html',
  styleUrls: ['./select-worker.component.css']
})
export class SelectWorkerComponent implements OnInit {

  selectedService!: Service | null;
  workers: User[] = [];
  displayedColumns: string[] = ["name", "surname", "phoneNumber", "email"];

  errorMessage: string = '';

  constructor(
    private visitService: VisitService,
    private workerService: WorkerService,
    private jwtTokenStorageService: JwtStorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    if (this.serviceHasBeenSelected()) {
      // @ts-ignore
      this.workerService.getAllByPerformedService(this.selectedService?.id).subscribe(
        workers => {
          this.workers = workers;
          this.deleteCurrentUserFromList();
        }, err => {
          this.errorMessage = 'There are no workers for selected service';
        }
      );
    } else {
      this.errorMessage = 'Error, service is not selected';
    }
  }

  private serviceHasBeenSelected(): boolean {
    this.selectedService = this.visitService.getSelectedService();
    return this.selectedService != null && this.selectedService.id != null;
  }

  private deleteCurrentUserFromList() {
    let currentUserId = this.jwtTokenStorageService.getCurrentUser()?.id;
    this.workers = this.workers.filter(client => client.id != currentUserId);
  }

  submitWorker(worker: User) {
    this.visitService.saveSelectedWorker(worker);
    this.router.navigate(['/visit/pick-term']);
  }
}
