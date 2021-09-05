export interface User {
  name: string,
  surname: string,
  phoneNumber: string,
  email: string,
  login: string,
  password: string,
  roles: Role[],
  masterRole: Role
}

export interface Role {
  id : number,
  name : string
}

export enum UserRole {
  ROLE_CLIENT = 1,
  ROLE_WORKER,
  ROLE_RECEPTIONIST,
  ROLE_ADMIN
}

export let roleNames: string[] = [
  'ROLE_CLIENT',
  'ROLE_WORKER',
  'ROLE_RECEPTIONIST',
  'ROLE_ADNIM'
];

export interface ResetPasswordRequest {
  resetToken: string,
  password: string,
  repeatPassword: string
}

