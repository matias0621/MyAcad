import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionCommision } from './inscription-commision';

describe('InscriptionCommision', () => {
  let component: InscriptionCommision;
  let fixture: ComponentFixture<InscriptionCommision>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCommision]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionCommision);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
