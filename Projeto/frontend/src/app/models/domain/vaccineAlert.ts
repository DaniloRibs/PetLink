export type VaccineAlertStatus = 'EXPIRED' | 'EXPIRING';

export interface VaccineAlert {
    animalId: number;
    animalName: string;
    species: string;
    farmAnimal: boolean;
    vaccineId: number;
    vaccineName: string;
    applicationDate: string;
    expirationDate: string;
    status: VaccineAlertStatus;
    daysOverdue: number;
    daysUntilExpiration: number;
}
