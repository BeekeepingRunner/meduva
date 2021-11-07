import { Component, OnInit } from '@angular/core';
import {VisitService} from "../../../service/visit.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {User} from "../../../model/user";
import {UserService} from "../../../service/user.service";

@Component({
  selector: 'app-visit-history',
  templateUrl: './visit-history.component.html',
  styleUrls: ['./visit-history.component.css']
})
export class VisitHistoryComponent implements OnInit {

  currentUserId!: number | undefined;

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private jwtStorage: JwtStorageService,
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.jwtStorage.getCurrentUser()?.id;
    if (this.currentUserId) {
      this.visitService.getAllAsClientByUserId(this.currentUserId).subscribe(
        visits => {
          console.log(visits);
        }, err => {
          console.log(err);
        }
      );
    }
  }

}
