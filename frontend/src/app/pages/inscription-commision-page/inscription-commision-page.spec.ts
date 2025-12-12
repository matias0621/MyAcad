import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionCommisionPage } from './inscription-commision-page';

describe('InscriptionCommisionPage', () => {
  let component: InscriptionCommisionPage;
  let fixture: ComponentFixture<InscriptionCommisionPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCommisionPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionCommisionPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
