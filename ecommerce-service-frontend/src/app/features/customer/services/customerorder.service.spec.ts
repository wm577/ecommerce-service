import { TestBed } from '@angular/core/testing';

import { CustomerorderService } from './customerorder.service';

describe('CustomerorderService', () => {
  let service: CustomerorderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerorderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
