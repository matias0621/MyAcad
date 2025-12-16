import { TestBed } from '@angular/core/testing';

import { CourseEvaluationService } from './course-evaluation-service';

describe('CourseEvaluationService', () => {
  let service: CourseEvaluationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseEvaluationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
