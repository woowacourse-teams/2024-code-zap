import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { postSignup } from '@/api/authentication';
import { useCheckEmailQuery } from '@/queries/authentication';

export const useSignupForm = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errors, setErrors] = useState({
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
  });

  const navigate = useNavigate();
  const { data: isUniqueEmail, isSuccess, refetch: checkEmailQuery } = useCheckEmailQuery(email);

  useEffect(() => {
    if (isSuccess) {
      if (isUniqueEmail?.check === false) {
        setErrors((prev) => ({
          ...prev,
          email: '중복된 이메일입니다.',
        }));
      }
    }
  }, [isUniqueEmail?.check, isSuccess]);

  const validateEmail = (email: string) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return regex.test(email) && !/\s/.test(email) ? '' : '유효한 이메일을 입력해주세요.';
  };

  const validateUsername = (username: string) => {
    const regex = /^[a-zA-Z0-9가-힣-_]+$/;

    return regex.test(username) && username.length > 0 ? '' : '공백을 제외한 1자 이상의 닉네임을 입력해주세요.';
  };

  const validatePassword = (password: string) =>
    password.length >= 6 && !/\s/.test(password) ? '' : '공백을 제외한 6자 이상의 비밀번호를 입력해주세요.';

  const validateConfirmPassword = (password: string, confirmPassword: string) =>
    password === confirmPassword ? '' : '비밀번호가 일치하지 않습니다.';

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setEmail(value);
    setErrors((prev) => ({ ...prev, email: validateEmail(value) }));
  };

  const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setUsername(value);
    setErrors((prev) => ({ ...prev, username: validateUsername(value) }));
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setPassword(value);
    setErrors((prev) => ({
      ...prev,
      password: validatePassword(value),
      confirmPassword: validateConfirmPassword(value, confirmPassword),
    }));
  };

  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setConfirmPassword(value);
    setErrors((prev) => ({
      ...prev,
      confirmPassword: validateConfirmPassword(password, value),
    }));
  };

  const isFormValid = () =>
    !errors.email &&
    !errors.username &&
    !errors.password &&
    !errors.confirmPassword &&
    email &&
    username &&
    password &&
    confirmPassword;

  const handleSubmit = async () => {
    if (isFormValid()) {
      const response = await postSignup({ email, username, password });

      if (!response.ok) {
        console.error(response);
      }

      navigate('/login');
    }
  };

  return {
    email,
    username,
    password,
    confirmPassword,
    errors,
    handleEmailChange,
    handleUsernameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
    checkEmailQuery,
  };
};
