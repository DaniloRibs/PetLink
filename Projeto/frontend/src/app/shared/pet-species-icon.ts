import { PetSpecies } from '../models/domain/pet';

export function speciesIcon(species: string): string {
    switch (species) {
        case PetSpecies.DOG: return '🐶';
        case PetSpecies.CAT: return '🐱';
        case PetSpecies.BIRD: return '🐦';
        case PetSpecies.HORSE: return '🐴';
        case PetSpecies.COW: return '🐄';
        case PetSpecies.PIG: return '🐷';
        case PetSpecies.SHEEP: return '🐑';
        default: return '';
    }
}