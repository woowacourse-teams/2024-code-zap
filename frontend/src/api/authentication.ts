import type { LoginRequest, SignupRequest } from '@/types';
import { MemberInfo } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const SIGNUP_API_URL = `${API_URL}/signup`;
export const LOGIN_API_URL = `${API_URL}/login`;
export const LOGOUT_API_URL = `${API_URL}/logout`;
export const LOGIN_STATE_API_URL = `${API_URL}/login/check`;
export const CHECK_USERNAME_API_URL = `${API_URL}/check-username`;
export const CHECK_EMAIL_API_URL = `${API_URL}/check-email`;

export const postSignup = async (signupInfo: SignupRequest) =>
  await customFetch({
    method: 'POST',
    url: `${SIGNUP_API_URL}`,
    body: JSON.stringify(signupInfo),
  });

export const postLogin = async (loginInfo: LoginRequest): Promise<MemberInfo> => {
  const response = await customFetch<MemberInfo>({
    method: 'POST',
    url: `${LOGIN_API_URL}`,
    body: JSON.stringify(loginInfo),
  });

  if ('memberId' in response) {
    return response;
  }

  if (response.status >= 400) {
    return { memberId: undefined, username: undefined };
  }

  throw new Error(response.detail);
};

export const postLogout = async () => {
  const response = await customFetch<unknown>({
    method: 'POST',
    url: `${LOGOUT_API_URL}`,
  });

  return response;
};

export const getLoginState = async () => {
  const url = `${LOGIN_STATE_API_URL}`;

  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  if (response.status === 401) {
    throw new Error('로그인을 해주세요.');
  }

  if (!response.ok) {
    throw new Error('서버 에러가 발생했습니다.');
  }

  return {};
};

export const checkEmail = async (email: string) => {
  const params = new URLSearchParams({ email });
  const url = `${CHECK_EMAIL_API_URL}?${params}`;

  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  if (response.status === 409) {
    throw new Error('중복된 이메일입니다.');
  }

  if (!response.ok) {
    throw new Error('서버 에러가 발생했습니다.');
  }

  return {};
};

export const checkUsername = async (username: string) => {
  const params = new URLSearchParams({ username });
  const url = `${CHECK_USERNAME_API_URL}?${params}`;

  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  if (response.status === 409) {
    throw new Error('중복된 닉네임입니다.');
  }

  if (!response.ok) {
    throw new Error('서버 에러가 발생했습니다.');
  }

  return {};
};
