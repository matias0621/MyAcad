import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramsForm } from './programs-form';

describe('ProgramsForm', () => {
  let component: ProgramsForm;
  let fixture: ComponentFixture<ProgramsForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramsForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgramsForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
