import { MessageResponse } from "./general.model";

export type Avatars = 'boy' | 'coder' | 'coder2' | 'death' | 'girl' | 'hacker' | 'man' | 'woman' | 'oldman' | 'oldwoman' | 'default';

export interface Profile {
  email: string;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  contactNumber: string;
  avatar: Avatars;
}

export interface UsernameValidation {
  username: string;
  usernameExists: boolean
}

export interface SigninResponse {
  messages: MessageResponse[];
  profile: Profile;
}

export interface EmailValidation {
  email: string;
  emailExists: boolean;
}

export interface ResetPassword {
  username: string;
  password: string;
}