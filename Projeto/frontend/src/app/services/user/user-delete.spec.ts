import { TestBed } from '@angular/core/testing';

import { UserDelete } from './user-delete';

describe('UserDelete', () => {
  let service: UserDelete;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserDelete);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
