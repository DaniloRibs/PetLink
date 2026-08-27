export enum PetSpecies {
    CACHORRO = 'cachorro',
    GATO = 'gato',
    PASSARO = 'passaro',
    OUTRO = 'outro',
}

export interface Pet {
    id?: string;
    name: string;
    species: PetSpecies;
    breed: string;
    birthDate: string;
    ownerEmail: string;

    // Usados na aba de Adoções: pet listado para doação e um recado
    // opcional de quem está doando. Opcionais para nao quebrar pets
    // ja cadastrados sem esses campos.
    forAdoption?: boolean;
    adoptionNote?: string;
}
