import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class SwalService {

  confirm(message: string, title: string = '¿Estás seguro?'): Promise<boolean> {
    return Swal.fire({
      title,
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e74c3c',
      cancelButtonColor: '#7f8c8d',
      confirmButtonText: 'Sí, continuar',
      cancelButtonText: 'Cancelar'
    }).then((result) => result.isConfirmed);
  }

  success(message: string, title: string = 'Éxito'): void {
    Swal.fire(title, message, 'success');
  }

  error(message: string, title: string = 'Error'): void {
    Swal.fire(title, message, 'error');
  }

  loading(message: string = 'Cargando...'): void {
    Swal.fire({
      title: message,
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
  }

  close(): void {
    Swal.close();
  }
}