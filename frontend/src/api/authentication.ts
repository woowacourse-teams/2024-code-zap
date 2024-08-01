import { SignupRequest } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const SIGNUP_API_URL = `${API_URL}/signup`;
export const CHECK_USERNAME_API_URL = `${API_URL}/check-username`;
export const CHECK_EMAIL_API_URL = `${API_URL}/check-email`;

export const postSignup = async (signupInfo: SignupRequest) =>
  await customFetch({
    method: 'POST',
    url: `${SIGNUP_API_URL}`,
    body: JSON.stringify(signupInfo),
  });

export const checkUsername = async (username: string) => {
  const params = new URLSearchParams({ username });
  const url = `${CHECK_USERNAME_API_URL}?${params}`;

  return await customFetch({
    url,
  });
};

export const checkEmail = async (email: string) => {
  const params = new URLSearchParams({ email });
  const url = `${CHECK_EMAIL_API_URL}?${params}`;

  return await customFetch({
    url,
  });
};
