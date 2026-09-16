export interface Adoption {
    id?: number;
    petId: number;
    ownerId: number;
    description: string;
    contact: string;
    adopted?: boolean;
    publicationDate?: string;
}
