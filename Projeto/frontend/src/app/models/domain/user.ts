export enum AccountType {
    PERSON = 'PERSON',
    ENTERPRISE = 'ENTERPRISE',
}

export interface User {
    id?: number,
    fullname: string,
    email: string,
    password: string,
    phone?: string,
    accountType?: AccountType,
    document?: string,
}

