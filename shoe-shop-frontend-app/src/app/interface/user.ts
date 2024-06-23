import { Role } from "./role";

export interface User {
    id?: number;
    avatar?: string;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber: string;
    address1: string;
    status: boolean;
    roles: Role[];
}