import { UserRole } from "./user-role";

export enum AccountType {
    PESSOA = 'pessoa',
    EMPRESA = 'empresa',
}

export interface User {
    id?: string,
    fullname: string,
    email: string,
    password: string,
    role: UserRole,

    // Campos adicionais usados no cadastro do PetLink (CriarConta.html).
    // Opcionais para nao quebrar o restante da base do professor.
    phone?: string,
    accountType?: AccountType,
    document?: string,
}

