import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecyficUserComponent } from './specyfic-user.component';

describe('SpecyficUserComponent', () => {
  let component: SpecyficUserComponent;
  let fixture: ComponentFixture<SpecyficUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpecyficUserComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecyficUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
