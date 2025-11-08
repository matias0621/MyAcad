import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionToExamList } from './inscription-to-exam-list';

describe('InscriptionToExamList', () => {
  let component: InscriptionToExamList;
  let fixture: ComponentFixture<InscriptionToExamList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionToExamList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionToExamList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
