import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-notificaciones-main',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './notificaciones-main.component.html',
  styleUrls: ['./notificaciones-main.component.css']
})
export class NotificacionesMainComponent { }