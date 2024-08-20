import { FormEvent, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useInputWithValidate } from '@/hooks/utils';
import { useCheckNameQuery, useSignupMutation } from '@/queries/authentication';
import { validateName, validatePassword, validateConfirmPassword } from '@/service';

export const useSignupForm = () => {
  const navigate = useNavigate();
  const { mutateAsync: postSignup } = useSignupMutation();

  const {
    value: name,
    errorMessage: nameError,
    handleChange: handleNameChange,
    handleErrorMessage: handleNameErrorMessage,
  } = useInputWithValidate('', validateName);

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

  const { refetch: checkNameQuery } = useCheckNameQuery(name);

  const handleNameCheck = async () => {
    const { error } = await checkNameQuery();

    // refetch does not exist onError
    if (error) {
      handleNameErrorMessage(error.message);
    }
  };

  // only change password not confirmPassword
  useEffect(() => {
    handleConfirmPasswordErrorMessage(validateConfirmPassword(password, confirmPassword));
  }, [password, confirmPassword, handleConfirmPasswordErrorMessage]);

  const isFormValid = () =>
    !nameError && !passwordError && !confirmPasswordError && name && password && confirmPassword;

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (isFormValid()) {
      await postSignup({ name, password });
      navigate('/login');
    }
  };

  return {
    name,
    password,
    confirmPassword,
    errors: {
      name: nameError,
      password: passwordError,
      confirmPassword: confirmPasswordError,
    },
    handleNameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
    handleNameCheck,
  };
};
