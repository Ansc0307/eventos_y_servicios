import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Categoria } from '../../models/categoria.model';


@Component({
  selector: 'app-categoria-filter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './categoria-filter.component.html'
})
export class CategoriaFilterComponent {
  @Input() categorias: any[] = [];
    @Input() selected: number | null = null;

    @Output() select = new EventEmitter<number>();

}
