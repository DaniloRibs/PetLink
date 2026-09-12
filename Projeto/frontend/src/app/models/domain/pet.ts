export enum PetSpecies {
    CACHORRO = 'cachorro',
    GATO = 'gato',
    PASSARO = 'passaro',
    OUTRO = 'outro',
}

export interface Pet {
    id?: number;
    name: string;
    species: PetSpecies;
    breed: string;
    birthDate: string;
    ownerEmail: string;
    forAdoption?: boolean;
    adoptionNote?: string;
}
