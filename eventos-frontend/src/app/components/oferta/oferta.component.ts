import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Oferta } from '../../models/oferta.model';
import { OfertasService } from '../../services/ofertas.service';

@Component({
  selector: 'app-oferta',
  templateUrl: './oferta.component.html',
  styleUrls: ['./oferta.component.css']
})
export class OfertaComponent implements OnInit {

  oferta?: Oferta;

  constructor(
    private route: ActivatedRoute,
    private ofertasService: OfertasService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
        this.ofertasService.obtenerOferta(Number(id)).subscribe(data => {
        this.oferta = data;
});

    }
  }

  editarOferta() {
    console.log("Editar oferta", this.oferta?.idOferta);
    // ejemplo:
    // this.router.navigate(['/proveedor/ofertas/editar', this.oferta?.idOferta]);
  }

  // ========================================
  // 🔵 Helpers: manejo universal de imágenes
  // ========================================

  private extractImages(): string[] {
    if (!this.oferta) return [];

    if (this.oferta.urlsMedia && this.oferta.urlsMedia.length > 0) {
      return this.oferta.urlsMedia;
    }

    if (this.oferta.medias && this.oferta.medias.length > 0) {
      return this.oferta.medias.map(m => m.url);
    }

    return [];
  }

  getMainImage(): string {
    const images = this.extractImages();
    return images[0] || 'assets/images/no-image.png';
  }

  getSecondaryImages(): string[] {
    const images = this.extractImages();
    return images.slice(1, 5);
  }

}
