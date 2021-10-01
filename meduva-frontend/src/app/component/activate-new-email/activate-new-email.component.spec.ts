import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateNewEmailComponent } from './activate-new-email.component';

describe('ActivateNewEmailComponent', () => {
  let component: ActivateNewEmailComponent;
  let fixture: ComponentFixture<ActivateNewEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActivateNewEmailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateNewEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
