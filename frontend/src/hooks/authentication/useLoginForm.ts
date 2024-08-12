import { FormEvent } from 'react';

import { useLoginMutation } from '@/queries/authentication/useLoginMutation';
import { useInputWithValidate } from '../useInputWithValidate';
import { validateEmail, validatePassword } from './validates';

export const useLoginForm = () => {
  const { mutateAsync } = useLoginMutation();

  const {
    value: email,
    errorMessage: emailError,
    handleChange: handleEmailChange,
  } = useInputWithValidate('', validateEmail);

  const {
    value: password,
    errorMessage: passwordError,
    handleChange: handlePasswordChange,
  } = useInputWithValidate('', validatePassword);

  const isFormValid = () => !emailError && !passwordError && email && password;

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (isFormValid()) {
      await mutateAsync({ email, password });
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
