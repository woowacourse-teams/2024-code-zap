import { END_POINTS } from '@/routes';
import type { LoginRequest, SignupRequest } from '@/types';

import { apiClient } from './config';

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
