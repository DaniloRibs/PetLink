import { PetGender, PetSpecies } from '../domain/pet';

export interface CreateFarmAnimalDto {
    name?: string;
    identify: string;
    species: PetSpecies;
    breed: string;
    birthDate: string;
    gender: PetGender;
    weight: number;
    ownerId: number;
}