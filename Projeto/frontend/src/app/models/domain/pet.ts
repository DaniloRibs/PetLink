export enum PetSpecies {
    CACHORRO = 'DOG',
    GATO = 'CAT',
    PASSARO = 'BIRD',
    OUTRO = 'OTHER',
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
