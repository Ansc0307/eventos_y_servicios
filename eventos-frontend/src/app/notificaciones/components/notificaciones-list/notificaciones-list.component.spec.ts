import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificacionesListComponent } from './notificaciones-list.component';

describe('NotificacionesList', () => {
  let component: NotificacionesListComponent;
  let fixture: ComponentFixture<NotificacionesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificacionesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificacionesListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
