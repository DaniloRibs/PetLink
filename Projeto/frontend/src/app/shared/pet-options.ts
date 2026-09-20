import { Pet, PetGender, PetSpecies } from '../models/domain/pet';

export interface SpeciesOption {
    value: PetSpecies;
    label: string;
}

export const PET_SPECIES_OPTIONS: SpeciesOption[] = [
    { value: PetSpecies.DOG, label: 'Cachorro' },
    { value: PetSpecies.CAT, label: 'Gato' },
    { value: PetSpecies.BIRD, label: 'Pássaro' },
    { value: PetSpecies.OTHER, label: 'Outro' },
];

export const FARM_SPECIES_OPTIONS: SpeciesOption[] = [
    { value: PetSpecies.HORSE, label: 'Cavalo' },
    { value: PetSpecies.COW, label: 'Vaca' },
    { value: PetSpecies.PIG, label: 'Porco' },
    { value: PetSpecies.SHEEP, label: 'Ovelha' },
    { value: PetSpecies.OTHER, label: 'Outro' },
];

const FARM_SPECIES: PetSpecies[] = [
    PetSpecies.HORSE,
    PetSpecies.COW,
    PetSpecies.PIG,
    PetSpecies.SHEEP,
];

export function genderLabel(gender?: string): string {
    switch (gender) {
        case PetGender.MALE: return 'Macho';
        case PetGender.FEMALE: return 'Fêmea';
        default: return '';
    }
}

export function filterByMode(pets: Pet[], isFarm: boolean): Pet[] {
    return pets.filter(pet => {
        if (pet.species === PetSpecies.OTHER) {
            return true;
        }
        return FARM_SPECIES.includes(pet.species) === isFarm;
    });
}