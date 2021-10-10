import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPerformedServicesComponent } from './edit-performed-services.component';

describe('EditPerformedServicesComponent', () => {
  let component: EditPerformedServicesComponent;
  let fixture: ComponentFixture<EditPerformedServicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditPerformedServicesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditPerformedServicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
