import { FormEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { postSignup } from '@/api/authentication';
import { useCheckEmailQuery } from '@/queries/authentication';
import { useCheckUsernameQuery } from '@/queries/authentication/useCheckUsernameQuery';
import { validateConfirmPassword, validateEmail, validatePassword, validateUsername } from './validates';

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
  const {
    data: isUniqueEmail,
    isSuccess: isSuccessCheckEmailQuery,
    refetch: checkEmailQuery,
  } = useCheckEmailQuery(email);
  const {
    data: isUniqueUsername,
    isSuccess: isSuccessCheckUsernameQuery,
    refetch: checkUserQuery,
  } = useCheckUsernameQuery(username);

  useEffect(() => {
    if (isSuccessCheckEmailQuery) {
      if (isUniqueEmail?.check === false) {
        setErrors((prev) => ({
          ...prev,
          email: '중복된 이메일입니다.',
        }));
      }
    }
  }, [isUniqueEmail?.check, isSuccessCheckEmailQuery]);

  useEffect(() => {
    if (isSuccessCheckUsernameQuery) {
      if (isUniqueUsername?.check === false) {
        setErrors((prev) => ({
          ...prev,
          username: '중복된 닉네임입니다.',
        }));
      }
    }
  }, [isUniqueUsername?.check, isSuccessCheckUsernameQuery]);

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

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (isFormValid()) {
      const response = await postSignup({ email, username, password });

      if (!response.ok) {
        console.error(response);

        return;
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
    checkUserQuery,
  };
};
