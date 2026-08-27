export interface Vaccine {
    id?: string;
    petId: string;
    name: string;
    applicationDate: string;
    nextDoseDate?: string;
    notes?: string;
}
