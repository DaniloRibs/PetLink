import { TestBed } from '@angular/core/testing';

import { UserRead } from './user-read';

describe('UserRead', () => {
  let service: UserRead;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRead);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
