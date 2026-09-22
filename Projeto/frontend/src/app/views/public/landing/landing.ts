import { Component, OnDestroy, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { AnimalMode } from '../../../models/domain/animalMode';
import { AnimalModeService } from '../../../services/animalMode/animalMode';

@Component({
  selector: 'app-landing',
  imports: [RouterLink, MatIconModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing implements OnInit, OnDestroy {

  readonly AnimalMode = AnimalMode;

  mode: AnimalMode = AnimalMode.PET;
  showAbout: boolean = false;

  private readonly platformId = inject(PLATFORM_ID);
  private autoRotateTimer: ReturnType<typeof setInterval> | null = null;
  private userChoseMode: boolean = false;

  constructor(private animalModeService: AnimalModeService) { }

  get isFarm(): boolean {
    return this.mode === AnimalMode.FARM;
  }

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }

    this.autoRotateTimer = setInterval(() => {
      this.mode = this.isFarm ? AnimalMode.PET : AnimalMode.FARM;
    }, 12000);
  }

  ngOnDestroy(): void {
    this.stopAutoRotate();
  }

  setMode(mode: AnimalMode): void {
    this.stopAutoRotate();
    this.userChoseMode = true;
    this.mode = mode;
  }

  rememberMode(mode?: AnimalMode): void {
    if (mode) {
      this.animalModeService.set(mode);
      return;
    }

    if (this.userChoseMode) {
      this.animalModeService.set(this.mode);
    }
  }

  openAbout(): void {
    this.showAbout = true;
  }

  closeAbout(): void {
    this.showAbout = false;
  }

  private stopAutoRotate(): void {
    if (this.autoRotateTimer !== null) {
      clearInterval(this.autoRotateTimer);
      this.autoRotateTimer = null;
    }
  }
}