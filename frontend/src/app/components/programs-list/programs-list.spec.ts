import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramsList } from './programs-list';

describe('ProgramsList', () => {
  let component: ProgramsList;
  let fixture: ComponentFixture<ProgramsList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramsList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgramsList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
