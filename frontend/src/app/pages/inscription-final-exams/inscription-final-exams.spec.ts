import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionFinalExams } from './inscription-final-exams';

describe('InscriptionFinalExams', () => {
  let component: InscriptionFinalExams;
  let fixture: ComponentFixture<InscriptionFinalExams>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionFinalExams]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionFinalExams);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
