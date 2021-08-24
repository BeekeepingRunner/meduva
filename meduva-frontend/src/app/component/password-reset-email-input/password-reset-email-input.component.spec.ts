import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordResetEmailInputComponent } from './password-reset-email-input.component';

describe('PasswordResetEmailInputComponent', () => {
  let component: PasswordResetEmailInputComponent;
  let fixture: ComponentFixture<PasswordResetEmailInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordResetEmailInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordResetEmailInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
