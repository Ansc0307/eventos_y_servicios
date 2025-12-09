import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

export interface ModalField {
  name: string;
  label: string;
  type: 'text' | 'number' | 'textarea' | 'select';
  required?: boolean;
  options?: { id: any; nombre: string }[];
  value?: any;
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  abrirModal(
    title: string, 
    fields: ModalField[], 
    confirmButtonText: string = 'Guardar'
  ): Promise<any> {
    // Construir el HTML del formulario
    let html = '';
    fields.forEach(field => {
      const value = field.value || '';
      
      if (field.type === 'textarea') {
        html += `
          <div class="swal-field-group">
            <label for="swal-${field.name}">${field.label} ${field.required ? '*' : ''}</label>
            <textarea id="swal-${field.name}" class="swal2-textarea" placeholder="${field.label}" ${field.required ? 'required' : ''}>${value}</textarea>
          </div>
        `;
      } else if (field.type === 'select') {
        let options = `<option value="">Selecciona ${field.label.toLowerCase()}</option>`;
        field.options?.forEach(option => {
          const selected = option.id === value ? 'selected' : '';
          options += `<option value="${option.id}" ${selected}>${option.nombre}</option>`;
        });
        
        html += `
          <div class="swal-field-group">
            <label for="swal-${field.name}">${field.label} ${field.required ? '*' : ''}</label>
            <select id="swal-${field.name}" class="swal2-select" ${field.required ? 'required' : ''}>
              ${options}
            </select>
          </div>
        `;
      } else {
        html += `
          <div class="swal-field-group">
            <label for="swal-${field.name}">${field.label} ${field.required ? '*' : ''}</label>
            <input type="${field.type}" id="swal-${field.name}" class="swal2-input" 
                   placeholder="${field.label}" value="${value}" ${field.required ? 'required' : ''}>
          </div>
        `;
      }
    });

    return Swal.fire({
      title,
      html: `
        <form id="swal-form">
          ${html}
        </form>
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText,
      cancelButtonText: 'Cancelar',
      preConfirm: () => {
        const formData: any = {};
        let isValid = true;

        fields.forEach(field => {
          const element = document.getElementById(`swal-${field.name}`) as HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement;
          if (element) {
            formData[field.name] = element.value;
            
            // Validación de campos requeridos
            if (field.required && !element.value) {
              isValid = false;
              Swal.showValidationMessage(`El campo "${field.label}" es obligatorio`);
            }
          }
        });

        return isValid ? formData : false;
      }
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        return result.value;
      }
      return null;
    });
  }
}