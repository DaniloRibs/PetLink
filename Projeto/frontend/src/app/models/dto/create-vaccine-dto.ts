export interface CreateVaccineDto {
    idPet: number;
    name: string;
    description?: string;
    applicationDate: string;
    expirationDate?: string;
    lote?: string;
}
