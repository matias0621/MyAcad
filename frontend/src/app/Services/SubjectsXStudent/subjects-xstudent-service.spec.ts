import { TestBed } from '@angular/core/testing';

import { SubjectsXStudentService } from './subjects-xstudent-service';

describe('SubjectsXStudentService', () => {
  let service: SubjectsXStudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubjectsXStudentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
