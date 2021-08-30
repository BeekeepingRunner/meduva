import { TestBed } from '@angular/core/testing';

import { ResetTokenService } from './reset-token.service';

describe('ResetTokenService', () => {
  let service: ResetTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResetTokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
