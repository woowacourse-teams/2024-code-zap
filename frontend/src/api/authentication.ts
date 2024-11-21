import { END_POINTS } from '@/routes';
import type { LoginRequest, SignupRequest } from '@/types';

import { apiClient } from './config';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const SIGNUP_API_URL = `${API_URL}${END_POINTS.SIGNUP}`;
export const LOGIN_API_URL = `${API_URL}${END_POINTS.LOGIN}`;
export const LOGOUT_API_URL = `${API_URL}${END_POINTS.LOGOUT}`;
export const LOGIN_STATE_API_URL = `${API_URL}${END_POINTS.LOGIN_CHECK}`;
export const CHECK_NAME_API_URL = `${API_URL}${END_POINTS.CHECK_NAME}`;

export const postSignup = async (signupInfo: SignupRequest) => await apiClient.post(END_POINTS.SIGNUP, signupInfo);

export const postLogin = async (loginInfo: LoginRequest) => {
  const response = await apiClient.post(END_POINTS.LOGIN, loginInfo);

  return await response.json();
};

export const postLogout = async () => await apiClient.post(END_POINTS.LOGOUT, {});

export const getLoginState = async () => apiClient.get(END_POINTS.LOGIN_CHECK);

export const checkName = async (name: string) => {
  const params = new URLSearchParams({ name });

  await apiClient.get(`${END_POINTS.CHECK_NAME}?${params.toString()}`);
};
