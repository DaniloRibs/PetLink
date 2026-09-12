export enum AccountType {
    PESSOA = 'PERSON',
    EMPRESA = 'ENTERPRISE',
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

