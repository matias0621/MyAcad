import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionToCommission } from './inscription-to-commission';

describe('InscriptionToCommission', () => {
  let component: InscriptionToCommission;
  let fixture: ComponentFixture<InscriptionToCommission>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionToCommission]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionToCommission);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
