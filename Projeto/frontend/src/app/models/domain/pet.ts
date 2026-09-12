export enum PetSpecies {
    DOG = 'DOG',
    CAT = 'CAT',
    BIRD = 'BIRD',
    OTHER = 'OTHER',
}

export interface Pet {
    id?: number;
    name: string;
    species: PetSpecies;
    breed: string;
    birthDate: string;
    ownerId: number;
    forAdoption?: boolean;
}
