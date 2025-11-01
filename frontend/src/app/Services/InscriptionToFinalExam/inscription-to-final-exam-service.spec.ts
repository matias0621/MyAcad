import { TestBed } from '@angular/core/testing';

import { InscriptionToFinalExamService } from './inscription-to-final-exam-service';

describe('InscriptionToFinalExamService', () => {
  let service: InscriptionToFinalExamService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionToFinalExamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
