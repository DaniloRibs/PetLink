import { Component, HostListener, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { AnimalModeService } from './services/animalMode/animalMode';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  valorInteiro: number = 20
  protected readonly title = signal('Projeto Faitec' + this.valorInteiro);

  constructor() {
    if (isPlatformBrowser(inject(PLATFORM_ID))) {
      inject(AnimalModeService).applyBranding();
    }
  }

  @HostListener('document:keydown.escape')
  closeTopModal(): void {
    const overlays = document.querySelectorAll<HTMLElement>('.modal-overlay, .sale-modal-overlay');
    overlays[overlays.length - 1]?.click();
  }
} 