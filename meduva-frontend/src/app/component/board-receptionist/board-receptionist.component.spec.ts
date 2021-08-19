import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardReceptionistComponent } from './board-receptionist.component';

describe('BoardReceptionistComponent', () => {
  let component: BoardReceptionistComponent;
  let fixture: ComponentFixture<BoardReceptionistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardReceptionistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardReceptionistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
