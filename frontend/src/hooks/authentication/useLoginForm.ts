import { FormEvent } from 'react';

import { useInputWithValidate } from '@/hooks/utils';
import { useLoginMutation } from '@/queries/authentication';
import { validateEmail, validatePassword } from '@/service';

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
