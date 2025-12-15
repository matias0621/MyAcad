import { TestBed } from '@angular/core/testing';

import { SubjectPrerequisiteService } from './subject-prerequisite-service';

describe('SubjectPrerequisiteService', () => {
  let service: SubjectPrerequisiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubjectPrerequisiteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
