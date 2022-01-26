import { Role } from "./role";

export interface User
{
    id: number;
    name: string;
    email: string;
    password: string;
    address: string;
    register_at: Date;
    enable: boolean;
    imageURL: string;
    role: Role;
}