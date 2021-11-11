import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Room} from "../../../../model/room";
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-number-of-rooms',
  templateUrl: './number-of-rooms.component.html',
  styleUrls: ['./number-of-rooms.component.css']
})
export class NumberOfRoomsComponent implements OnInit {

  numberOfRoomsGroup!: FormGroup;
  successMessage: string = '';

  roomItems: Room[] = [];
  @Output() roomItemsEmitter = new EventEmitter<Room[]>();
  @Output() stepCompletionEmitter = new EventEmitter<boolean>();

  constructor(
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.numberOfRoomsGroup = this.formBuilder.group({
      itemCount: new FormControl('',[
        Validators.required,
        Validators.min(1),
      ])
    });
  }

  validateForm(): void {
    this.generateItems();
    this.completeStep();
    this.successMessage = 'Success! Move to the next step';

  }

  generateItems(): void {
    let itemCount: number = this.numberOfRoomsGroup.controls.itemCount.value;

    this.roomItems = [];
    for(let i = 1; i <= itemCount; i++)
    {
      let roomItem: Room = {
        name: "Room " + i,
        description: "",
        deleted: false,
        services: []

      }
      this.roomItems.push(roomItem);
    }
  }

  completeStep(): void {
    this.roomItemsEmitter.emit(this.roomItems);
    this.stepCompletionEmitter.emit(true);
  }

}
