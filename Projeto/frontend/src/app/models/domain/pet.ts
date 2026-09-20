export enum PetSpecies {
    DOG = 'DOG',
    CAT = 'CAT',
    BIRD = 'BIRD',
    OTHER = 'OTHER',
    HORSE = 'HORSE',
    COW = 'COW',
    PIG = 'PIG',
    SHEEP = 'SHEEP',
}

export enum PetGender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
}

export interface Pet {
    id?: number;
    name: string;
    species: PetSpecies;
    gender?: PetGender;
    breed: string;
    birthDate: string;
    ownerId: number;
    forAdoption?: boolean;
    identifier?: string;
    weight?: number;
    forSell?: boolean;
}