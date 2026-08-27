import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { UserReadService } from './user-read';

describe('UserReadService', () => {
  let service: UserReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(UserReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
