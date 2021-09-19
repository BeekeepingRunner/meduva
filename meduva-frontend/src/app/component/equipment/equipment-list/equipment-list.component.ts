import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-equipment-list',
  templateUrl: './equipment-list.component.html',
  styleUrls: ['./equipment-list.component.css']
})
export class EquipmentListComponent implements OnInit {

  models: any;
  displayedColumns: string[] = ['name'];

  constructor() { }

  ngOnInit(): void {
  }

}
