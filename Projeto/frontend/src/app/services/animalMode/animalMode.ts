import { Injectable } from '@angular/core';
import { AnimalMode } from '../../models/domain/animalMode';

@Injectable({
    providedIn: 'root',
})
export class AnimalModeService {

    private readonly storageKey: string = 'animalMode';

    get(): AnimalMode {
        return localStorage.getItem(this.storageKey) === AnimalMode.FARM
            ? AnimalMode.FARM
            : AnimalMode.PET;
    }

    set(mode: AnimalMode): void {
        localStorage.setItem(this.storageKey, mode);
    }
}