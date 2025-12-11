import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-solicitud-detalle',
  standalone: true,
  templateUrl: './solicitud-detalle.component.html',
  styleUrls: ['./solicitud-detalle.component.css']
})
export class SolicitudDetalleComponent {

  @Input() solicitud: any;  // La solicitud enviada desde el dashboard
  @Input() visible: boolean = false;

  @Output() close = new EventEmitter<void>();

  cerrar() {
    this.close.emit();
  }
}
