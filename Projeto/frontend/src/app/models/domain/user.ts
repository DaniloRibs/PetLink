import { UserRole } from "./user-role";

export enum AccountType {
    PESSOA = 'pessoa',
    EMPRESA = 'empresa',
}

export interface User {
    id?: string;
    fullname: string;
    email: string;
    password: string;
    role: UserRole;
    phone?: string;
    accountType?: AccountType;
    document?: string;
}

