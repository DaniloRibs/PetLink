import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { AnimalMode } from '../../models/domain/animalMode';

@Injectable({
    providedIn: 'root',
})
export class AnimalModeService {

    private readonly document = inject(DOCUMENT);
    private readonly platformId = inject(PLATFORM_ID);
    private readonly isBrowser = isPlatformBrowser(this.platformId);
    private readonly storageKey: string = 'animalMode';

    private readonly branding = {
        [AnimalMode.PET]: { title: 'PetLink', icon: 'assets/images/PetLink_Logo_Marrom.png' },
        [AnimalMode.FARM]: { title: 'PetLink · Fazenda', icon: 'assets/images/PetLink_Logo_Verde.png' },
    };

    get(): AnimalMode {
        if (!this.isBrowser) {
            return AnimalMode.PET;
        }
        return localStorage.getItem(this.storageKey) === AnimalMode.FARM
            ? AnimalMode.FARM
            : AnimalMode.PET;
    }

    set(mode: AnimalMode): void {
        if (!this.isBrowser) {
            return;
        }
        localStorage.setItem(this.storageKey, mode);
        this.applyBranding(mode);
    }

    applyBranding(mode: AnimalMode = this.get()): void {
        if (!this.isBrowser) {
            return;
        }
        const { title, icon } = this.branding[mode];
        this.document.title = title;
        this.document.getElementById('app-favicon')?.setAttribute('href', icon);
    }
} 