import { AccountType } from '../domain/user';

export interface AutheticatedUserDto {
    id: number;
    email: string;
    fullname: string;
    phone?: string;
    accountType?: AccountType;
    document?: string;
}
