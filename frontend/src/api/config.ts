import { ApiClient } from './ApiClient';

export const API_URL = process.env.REACT_APP_API_URL ?? 'https://default-url.com';

const httpHeader = {
  'Content-Type': 'application/json',
};
const httpCredentials = 'include';

export const apiClient = new ApiClient(API_URL, httpHeader, httpCredentials);
