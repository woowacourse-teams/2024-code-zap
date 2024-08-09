import { FormEvent, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { postSignup } from '@/api/authentication';
import { useCheckEmailQuery } from '@/queries/authentication';
import { useCheckUsernameQuery } from '@/queries/authentication/useCheckUsernameQuery';
import { useInputWithValidate } from '../useInputWithValidate';
import { validateEmail, validateUsername, validatePassword, validateConfirmPassword } from './validates';

export const useSignupForm = () => {
  const navigate = useNavigate();

  const {
    value: email,
    errorMessage: emailError,
    handleChange: handleEmailChange,
    handleErrorMessage: handleEmailErrorMessage,
  } = useInputWithValidate('', validateEmail);

  const {
    value: username,
    errorMessage: usernameError,
    handleChange: handleUsernameChange,
    handleErrorMessage: handleUsernameErrorMessage,
  } = useInputWithValidate('', validateUsername);

  const {
    value: password,
    errorMessage: passwordError,
    handleChange: handlePasswordChange,
  } = useInputWithValidate('', validatePassword);

  const {
    value: confirmPassword,
    errorMessage: confirmPasswordError,
    handleChange: handleConfirmPasswordChange,
    handleErrorMessage: handleConfirmPasswordErrorMessage,
  } = useInputWithValidate('', (value, compareValue) => validateConfirmPassword(value, compareValue ?? ''));

  const { refetch: checkEmailQuery } = useCheckEmailQuery(email);
  const { refetch: checkUsernameQuery } = useCheckUsernameQuery(username);

  const handleEmailCheck = async () => {
    const { error } = await checkEmailQuery();

    // refetch does not exist onError
    if (error) {
      handleEmailErrorMessage(error.message);
    }
  };

  const handleUsernameCheck = async () => {
    const { error } = await checkUsernameQuery();

    // refetch does not exist onError
    if (error) {
      handleUsernameErrorMessage(error.message);
    }
  };

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
      await postSignup({ email, username, password });
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
    handleUsernameCheck,
  };
};
