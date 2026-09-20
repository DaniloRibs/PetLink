export enum PriceType {
    FIXED = 'FIXED',
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
    saleDate?: string;
    userId: number;
    contact: string;
}