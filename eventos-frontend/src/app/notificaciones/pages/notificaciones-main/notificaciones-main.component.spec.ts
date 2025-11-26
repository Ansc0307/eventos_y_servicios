import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificacionesMain } from './notificaciones-main';

describe('NotificacionesMain', () => {
  let component: NotificacionesMain;
  let fixture: ComponentFixture<NotificacionesMain>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificacionesMain]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificacionesMain);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
