import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomSelectionDialogComponent } from './room-selection-dialog.component';

describe('RoomSelectionDialogComponent', () => {
  let component: RoomSelectionDialogComponent;
  let fixture: ComponentFixture<RoomSelectionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoomSelectionDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
