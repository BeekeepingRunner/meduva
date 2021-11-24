import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditVisitTermComponent } from './edit-visit-term.component';

describe('EditVisitTermComponent', () => {
  let component: EditVisitTermComponent;
  let fixture: ComponentFixture<EditVisitTermComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditVisitTermComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditVisitTermComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
