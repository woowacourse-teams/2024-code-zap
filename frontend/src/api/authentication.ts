import { END_POINTS } from '@/routes';
import type { LoginRequest, SignupRequest } from '@/types';

import { apiClient } from './config';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const SIGNUP_API_URL = `${API_URL}${END_POINTS.SIGNUP}`;
export const LOGIN_API_URL = `${API_URL}${END_POINTS.LOGIN}`;
export const LOGOUT_API_URL = `${API_URL}${END_POINTS.LOGOUT}`;
export const LOGIN_STATE_API_URL = `${API_URL}${END_POINTS.LOGIN_CHECK}`;
export const CHECK_NAME_API_URL = `${API_URL}${END_POINTS.CHECK_NAME}`;

export const postSignup = async (signupInfo: SignupRequest) => await apiClient.post(`${END_POINTS.SIGNUP}`, signupInfo);

export const postLogin = async (loginInfo: LoginRequest) => {
  const response = await apiClient.post(`${END_POINTS.LOGIN}`, loginInfo);

  return await response.json();
};

export const postLogout = async () => {
  const response = await customFetch<unknown>({
    method: 'POST',
    url: `${LOGOUT_API_URL}`,
  });

  return response;
};

export const getLoginState = async () => apiClient.get(END_POINTS.LOGIN_CHECK);

export const checkName = async (name: string) => {
  const params = new URLSearchParams({ name });
  const url = `${CHECK_NAME_API_URL}?${params}`;

  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  if (response.status === 409) {
    throw new Error('중복된 아이디입니다.');
  }

  if (!response.ok) {
    throw new Error('서버 에러가 발생했습니다.');
  }

  return {};
};
