export interface LoginBody {
  document: number,
  password: string
}

export interface ResponseLogin {
  access_token: string
}

export interface ResponseRefresh {
  refresh_token: string
}

export interface JwtDecodeUser {
  iss: string,
  sub: string,
  document_number: string,
  business_id: string,
  iat: number,
  exp: number,
  groups: GroupsAuth[]
}

export enum GroupsAuth {
  ADMIN = "SP_ADMINISTRADOR",
  NOT_ROLE = "NOT_ROLE"
}