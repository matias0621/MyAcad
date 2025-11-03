import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Technicals } from './technicals';

describe('Technicals', () => {
  let component: Technicals;
  let fixture: ComponentFixture<Technicals>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Technicals]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Technicals);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
