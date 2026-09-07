import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { UserUpdateService } from './user-update';

describe('UserUpdateService', () => {
  let service: UserUpdateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(UserUpdateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
