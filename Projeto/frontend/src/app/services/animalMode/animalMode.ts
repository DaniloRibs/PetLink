import { Injectable, inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { AnimalMode } from '../../models/domain/animalMode';

@Injectable({
    providedIn: 'root',
})
export class AnimalModeService {

    private readonly document = inject(DOCUMENT);
    private readonly storageKey: string = 'animalMode';

    private readonly branding = {
        [AnimalMode.PET]: { title: 'PetLink', icon: 'assets/images/PetLink_Logo_Marrom.png' },
        [AnimalMode.FARM]: { title: 'PetLink · Fazenda', icon: 'assets/images/PetLink_Logo_Verde.png' },
    };

    get(): AnimalMode {
        return localStorage.getItem(this.storageKey) === AnimalMode.FARM
            ? AnimalMode.FARM
            : AnimalMode.PET;
    }

    set(mode: AnimalMode): void {
        localStorage.setItem(this.storageKey, mode);
        this.applyBranding(mode);
    }

    applyBranding(mode: AnimalMode = this.get()): void {
        const { title, icon } = this.branding[mode];
        this.document.title = title;
        this.document.getElementById('app-favicon')?.setAttribute('href', icon);
    }
} 