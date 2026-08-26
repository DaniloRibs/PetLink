import { TestBed } from '@angular/core/testing';

import { UserUpdate } from './user-update';

describe('UserUpdate', () => {
  let service: UserUpdate;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserUpdate);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
