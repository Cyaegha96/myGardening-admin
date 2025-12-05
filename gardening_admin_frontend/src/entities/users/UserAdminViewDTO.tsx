export interface UserAdminViewDTO {
    id: string; //uuid
    uuid: string;
    status: string;
    createdAt: string;
    updatedAt: string;
    provider?: string;
    userId?:string;
    providerUserId?: string;
    email?: string;
    phone?: string;

    name?: string;
    nickname?: string;
    address?: string;
    zipcode?: string;
    bio?: string;
    profileUrl?: string;
    birthDate?: string;
    roles?: string[];
}
