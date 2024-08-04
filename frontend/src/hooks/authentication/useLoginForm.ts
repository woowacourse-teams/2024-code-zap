import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';

import { postLogin } from '@/api/authentication';
import { useInput } from '../useInput';
import { validateEmail, validatePassword } from './validates';

export const useLoginForm = () => {
  const navigate = useNavigate();

  const { value: email, errorMessage: emailError, handleChange: handleEmailChange } = useInput('', validateEmail);

  const {
    value: password,
    errorMessage: passwordError,
    handleChange: handlePasswordChange,
  } = useInput('', validatePassword);

  const isFormValid = () => !emailError && !passwordError && email && password;

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (isFormValid()) {
      const response = await postLogin({ email, password });

      if (!response.ok) {
        console.error(response);

        return;
      }

      navigate('/');
    }
  };

  return {
    email,
    password,
    errors: {
      email: emailError,
      password: passwordError,
    },
    handleEmailChange,
    handlePasswordChange,
    isFormValid,
    handleSubmit,
  };
};
