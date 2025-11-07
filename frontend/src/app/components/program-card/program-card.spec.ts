import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramCard } from './program-card';

describe('ProgramCard', () => {
  let component: ProgramCard;
  let fixture: ComponentFixture<ProgramCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgramCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
