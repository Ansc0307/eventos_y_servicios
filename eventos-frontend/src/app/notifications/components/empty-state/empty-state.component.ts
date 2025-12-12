import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empty-state.component.html',
  //styleUrls: ['./empty-state.component.css']
})
export class EmptyStateComponent {
  @Input() titulo: string = 'No hay datos';
  @Input() mensaje: string = 'No se encontraron elementos para mostrar.';
  @Input() mostrarBoton: boolean = false;
  @Input() textoBoton: string = 'Recargar';
  @Output() botonClick = new EventEmitter<void>();
}