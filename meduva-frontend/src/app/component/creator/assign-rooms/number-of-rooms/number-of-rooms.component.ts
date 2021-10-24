import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EquipmentService} from "../../../../service/equipment.service";
import {EquipmentItem} from "../../../../model/equipment";

@Component({
  selector: 'app-number-of-rooms',
  templateUrl: './number-of-rooms.component.html',
  styleUrls: ['./number-of-rooms.component.css']
})
export class NumberOfRoomsComponent implements OnInit {

  numberOfRoomsGroup!: FormGroup;
  successMessage: string = '';

  eqItems: EquipmentItem[] = [];
  @Output() eqItemsEmitter = new EventEmitter<EquipmentItem[]>();
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

    this.eqItems = [];
    for(let i = 1; i <= itemCount; i++)
    {
      let eqItem: EquipmentItem = {
        id: i,
        name: "Room " + i
      }
      this.eqItems.push(eqItem);
    }
  }

  completeStep(): void {
    this.eqItemsEmitter.emit(this.eqItems);
    this.stepCompletionEmitter.emit(true);
  }

}
