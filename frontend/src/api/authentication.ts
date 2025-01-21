import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';
import type { LoginRequest, SignupRequest } from '@/types';

export const postSignup = async (signupInfo: SignupRequest) => await apiClient.post(END_POINTS.SIGNUP, signupInfo);

export const postLogin = async (loginInfo: LoginRequest) => await apiClient.post(END_POINTS.LOGIN, loginInfo);

export const postLogout = async () => await apiClient.post(END_POINTS.LOGOUT, {});

export const getLoginState = async () => await apiClient.get(END_POINTS.LOGIN_CHECK);

export const checkName = async (name: string) => {
  const params = new URLSearchParams({ name });

  return await apiClient.get(`${END_POINTS.CHECK_NAME}?${params.toString()}`);
};
