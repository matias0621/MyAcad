import { TestBed } from '@angular/core/testing';

import { InscriptionToCommissionService } from './inscription-to-commission-service';

describe('InscriptionToCommissionService', () => {
  let service: InscriptionToCommissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionToCommissionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
