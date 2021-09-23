import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicesSelectComponent } from './services-select.component';

describe('ServicesSelectComponent', () => {
  let component: ServicesSelectComponent;
  let fixture: ComponentFixture<ServicesSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ServicesSelectComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ServicesSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
