import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from "../../../../model/user";
import {Service} from "../../../../model/service";
import {WorkerService} from "../../../../service/worker.service";
import {JwtStorageService} from "../../../../service/token/jwt-storage.service";

@Component({
  selector: 'app-worker-selection',
  templateUrl: './worker-selection.component.html',
  styleUrls: ['./worker-selection.component.css']
})
export class WorkerSelectionComponent implements OnInit {

  @Input()
  selectedService!: Service;

  workers: User[] = [];
  @Output() workerEmitter = new EventEmitter<User>();
  @Output() reRenderSignalEmitter = new EventEmitter<boolean>();

  displayedColumns: string[] = ['name', 'surname', 'phoneNumber', 'email'];

  errorMessage: string = '';

  constructor(
    private workerService: WorkerService,
    private jwtStorage: JwtStorageService,
  ) { }

  ngOnInit(): void {
    if (this.selectedService.id) {
      this.workerService.getAllByPerformedService(this.selectedService.id).subscribe(
        workers => {
          this.workers = workers;
          this.deleteCurrentUserFromList();
        }, err => {
          this.errorMessage = 'There are no workers for selected service';
        }
      );
    }
  }

  private deleteCurrentUserFromList() {
    let currentUserId = this.jwtStorage.getCurrentUser()?.id;
    this.workers = this.workers.filter(client => client.id != currentUserId);
  }

  onWorkerSelect(worker: User) {
    this.reRenderSignalEmitter.emit(true);
    this.workerEmitter.emit(worker);
  }
}
