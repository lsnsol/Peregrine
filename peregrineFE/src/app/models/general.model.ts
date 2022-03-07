export type AlertColor = 'green' | 'blue' | 'yellow' | 'red' | 'neutral';

export type DateTypes = 'short' | 'medium' | 'long' | 'full' | 'shortDate' | 'mediumDate' | 'longDate' | 'fullDate' | 'shortTime' | 'mediumTime' | 'longTime' | 'fullTime';

export const PASSWORD_REGEX: RegExp = new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,15}$/);

export const USERNAME_REGEX: RegExp = new RegExp(/^[A-Za-z][\w]{7,29}$/);

export const HTTP_REQUEST_HEADER = {
  'Content-Type': 'application/json'
}

export interface MessageResponse {
  code: string;
  message: string;
  type: string;
}

export interface MessageOnlyResponse {
  messages: MessageResponse[];
}

export interface IkeyValuePair<T> {
  [id: string]: T;
}