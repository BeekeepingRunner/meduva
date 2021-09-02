import { TestBed } from '@angular/core/testing';

import { JwtTokenStorageService } from './jwt-token-storage.service';

describe('TokenStorageService', () => {
  let service: JwtTokenStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JwtTokenStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
