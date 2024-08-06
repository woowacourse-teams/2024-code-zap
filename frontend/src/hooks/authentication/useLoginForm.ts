import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';

import { postLogin } from '@/api/authentication';
import { ToastContext } from '@/context/ToastContext';
import { useInputWithValidate } from '../useInputWithValidate';
import useCustomContext from '../utils/useCustomContext';
import { validateEmail, validatePassword } from './validates';

export const useLoginForm = () => {
  const navigate = useNavigate();
  const { failAlert, successAlert } = useCustomContext(ToastContext);

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
      const response = await postLogin({ email, password });

      if (!response.ok) {
        console.error(response);
        failAlert('로그인에 실패하였습니다.');

        return;
      }

      navigate('/');
      successAlert('로그인 성공!');
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
