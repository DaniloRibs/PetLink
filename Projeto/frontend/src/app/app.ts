import { Component, HostListener, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  valorInteiro: number = 20
  protected readonly title = signal('Projeto Faitec' + this.valorInteiro);

  @HostListener('document:keydown.escape')
  closeTopModal(): void {
    const overlays = document.querySelectorAll<HTMLElement>('.modal-overlay, .sale-modal-overlay');
    overlays[overlays.length - 1]?.click();
  }
} 