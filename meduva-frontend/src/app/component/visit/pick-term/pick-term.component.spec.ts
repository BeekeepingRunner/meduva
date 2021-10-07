import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickTermComponent } from './pick-term.component';

describe('PickTermComponent', () => {
  let component: PickTermComponent;
  let fixture: ComponentFixture<PickTermComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickTermComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickTermComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
