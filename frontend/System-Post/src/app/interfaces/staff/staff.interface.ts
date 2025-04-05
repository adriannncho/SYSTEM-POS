export interface DocumentType {
    idTypeDocument: number;
    name: string;
}

export interface UserType {
    idTypeUser: number;
    name: string;
}

export interface Business {
    idBusiness: number;
    name: string;
    typeBusiness: string;
    address: string;
}

export interface User {
    documentNumber: number;
    documentTypeId: DocumentType;
    userType: UserType;
    businessId: Business;
    userStatus: boolean;
    name: string;
    phone: string;
    email: string;
    password: string;
}
