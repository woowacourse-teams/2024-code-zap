import { useState } from 'react';

import { postSignup } from '@/api/authentication';

export const useSignupForm = () => {
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errors, setErrors] = useState({
    email: '',
    nickname: '',
    password: '',
    confirmPassword: '',
  });

  const validateEmail = (email: string) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return regex.test(email) ? '' : '유효한 이메일을 입력해주세요.';
  };

  const validateNickname = (nickname: string) => (nickname ? '' : '닉네임을 입력해주세요.');

  const validatePassword = (password: string) => (password.length >= 6 ? '' : '비밀번호는 최소 6자 이상이어야 합니다.');

  const validateConfirmPassword = (password: string, confirmPassword: string) =>
    password === confirmPassword ? '' : '비밀번호가 일치하지 않습니다.';

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    setEmail(value);
    setErrors((prev) => ({ ...prev, email: validateEmail(value) }));
  };

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    setNickname(value);
    setErrors((prev) => ({ ...prev, nickname: validateNickname(value) }));
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    setPassword(value);
    setErrors((prev) => ({
      ...prev,
      password: validatePassword(value),
      confirmPassword: validateConfirmPassword(value, confirmPassword),
    }));
  };

  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    setConfirmPassword(value);
    setErrors((prev) => ({
      ...prev,
      confirmPassword: validateConfirmPassword(password, value),
    }));
  };

  const isFormValid = () =>
    !errors.email &&
    !errors.nickname &&
    !errors.password &&
    !errors.confirmPassword &&
    email &&
    nickname &&
    password &&
    confirmPassword;

  const handleSubmit = async () => {
    if (isFormValid()) {
      const response = await postSignup({ email, nickname, password, confirmPassword });

      if (!response.ok) {
        console.error(response);
      }

      console.log('signup success');
      // route to Login
    }
  };

  return {
    email,
    nickname,
    password,
    confirmPassword,
    errors,
    handleEmailChange,
    handleNicknameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
  };
};
