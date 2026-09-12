export interface Vaccine {
    id?: number;
    petId: number;
    name: string;
    applicationDate: string;
    nextDoseDate?: string;
    notes?: string;
}
