import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomService} from "../../../../service/room.service";
import {Room} from "../../../../model/room";

@Component({
  selector: 'app-new-room',
  templateUrl: './new-room.component.html',
  styleUrls: ['./new-room.component.css']
})
export class NewRoomComponent implements OnInit {

  form!: FormGroup;
  errorMessage: any;
  didAddingFail: boolean = false;
  isSubmitted: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private roomService: RoomService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
        name: new FormControl('', [
          Validators.required
        ]),
        description: new FormControl('')
      }
    );
  }

  addRoom() {
    let room: Room = {
      name: this.form.controls.name.value,
      description: this.form.controls.description.value,
      deleted: false
    };

    this.roomService.addNewRoom(room).subscribe(
      data => {
        this.didAddingFail = false;
        this.isSubmitted = true;
      },
      err => {
        this.didAddingFail = true;
        this.errorMessage = err.error.message;
        console.log(err)
      }
    )
  }
}
