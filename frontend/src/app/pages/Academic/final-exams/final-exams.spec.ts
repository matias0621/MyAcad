import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalExams } from './final-exams';

describe('FinalExams', () => {
  let component: FinalExams;
  let fixture: ComponentFixture<FinalExams>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinalExams]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinalExams);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
