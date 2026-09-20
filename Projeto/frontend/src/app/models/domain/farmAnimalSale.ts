export enum PriceType {
    MANUAL = 'MANUAL',
    PER_ARROBA = 'PER_ARROBA',
}

export const KG_PER_ARROBA = 15;

export interface FarmAnimalSale {
    id?: number;
    description: string;
    farmAnimalIds: number[];
    priceType: PriceType;
    pricePerArroba?: number | null;
    price: number;
    userId: number;
    contact: string;
}