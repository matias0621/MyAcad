import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramsEditForm } from './programs-edit-form';

describe('ProgramsEditForm', () => {
  let component: ProgramsEditForm;
  let fixture: ComponentFixture<ProgramsEditForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramsEditForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgramsEditForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
