import { FormEvent, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { postSignup } from '@/api/authentication';
import { useCheckEmailQuery } from '@/queries/authentication';
import { useCheckUsernameQuery } from '@/queries/authentication/useCheckUsernameQuery';
import { useInput } from '../useInput';
import { validateEmail, validateUsername, validatePassword, validateConfirmPassword } from './validates';

export const useSignupForm = () => {
  const navigate = useNavigate();

  const {
    value: email,
    errorMessage: emailError,
    handleChange: handleEmailChange,
    handleErrorMessage: handleEmailErrorMessage,
  } = useInput('', validateEmail);

  const {
    value: username,
    errorMessage: usernameError,
    handleChange: handleUsernameChange,
    handleErrorMessage: handleUsernameErrorMessage,
  } = useInput('', validateUsername);

  const {
    value: password,
    errorMessage: passwordError,
    handleChange: handlePasswordChange,
  } = useInput('', validatePassword);

  const {
    value: confirmPassword,
    errorMessage: confirmPasswordError,
    handleChange: handleConfirmPasswordChange,
    handleErrorMessage: handleConfirmPasswordErrorMessage,
  } = useInput('', (value, compareValue) => validateConfirmPassword(value, compareValue ?? ''));

  const { refetch: checkEmailQuery } = useCheckEmailQuery(email);
  const {
    data: isUniqueUsername,
    isSuccess: isSuccessCheckUsernameQuery,
    refetch: checkUserQuery,
  } = useCheckUsernameQuery(username);

  const handleEmailCheck = async () => {
    const { error } = await checkEmailQuery();

    // refetch does not exist onError
    if (error) {
      handleEmailErrorMessage(error.message);
    }
  };

  useEffect(() => {
    if (isSuccessCheckUsernameQuery) {
      if (isUniqueUsername?.check === false) {
        handleUsernameErrorMessage('중복된 닉네임입니다.');
      }
    }
  }, [isUniqueUsername?.check, isSuccessCheckUsernameQuery, handleUsernameErrorMessage]);

  // only change password not confirmPassword
  useEffect(() => {
    handleConfirmPasswordErrorMessage(validateConfirmPassword(password, confirmPassword));
  }, [password, confirmPassword, handleConfirmPasswordErrorMessage]);

  const isFormValid = () =>
    !emailError &&
    !usernameError &&
    !passwordError &&
    !confirmPasswordError &&
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
    errors: {
      email: emailError,
      username: usernameError,
      password: passwordError,
      confirmPassword: confirmPasswordError,
    },
    handleEmailChange,
    handleUsernameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
    handleEmailCheck,
    checkUserQuery,
  };
};
