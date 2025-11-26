import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrioridadesList } from './prioridades-list.component';

describe('PrioridadesList', () => {
  let component: PrioridadesList;
  let fixture: ComponentFixture<PrioridadesList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrioridadesList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrioridadesList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
