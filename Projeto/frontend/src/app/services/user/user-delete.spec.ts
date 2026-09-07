import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { UserDeleteService } from './user-delete';

describe('UserDeleteService', () => {
  let service: UserDeleteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(UserDeleteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
