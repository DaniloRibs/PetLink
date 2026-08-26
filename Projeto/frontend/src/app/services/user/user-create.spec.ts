import { TestBed } from '@angular/core/testing';

import { UserCreate } from './user-create';

describe('UserCreate', () => {
  let service: UserCreate;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCreate);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
