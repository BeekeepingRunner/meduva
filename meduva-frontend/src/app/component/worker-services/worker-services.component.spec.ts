import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerServicesComponent } from './worker-services.component';

describe('WorkerServicesComponent', () => {
  let component: WorkerServicesComponent;
  let fixture: ComponentFixture<WorkerServicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkerServicesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkerServicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
