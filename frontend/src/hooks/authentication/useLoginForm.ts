import { FormEvent } from 'react';

import { useInputWithValidate } from '@/hooks/utils';
import { useLoginMutation } from '@/queries/authentication';
import { validateName, validatePassword } from '@/service';

export const useLoginForm = () => {
  const { mutateAsync } = useLoginMutation();

  const {
    value: name,
    errorMessage: nameError,
    handleChange: handleNameChange,
  } = useInputWithValidate('', validateName);

  const {
    value: password,
    errorMessage: passwordError,
    handleChange: handlePasswordChange,
  } = useInputWithValidate('', validatePassword);

  const isFormValid = () => !nameError && !passwordError && name && password;

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (isFormValid()) {
      await mutateAsync({ name, password });
    }
  };

  return {
    name,
    password,
    errors: {
      name: nameError,
      password: passwordError,
    },
    handleNameChange,
    handlePasswordChange,
    isFormValid,
    handleSubmit,
  };
};
