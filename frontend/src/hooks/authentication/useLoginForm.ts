import { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { postLogin } from '@/api/authentication';
import { validateEmail, validatePassword } from './validates';

export const useLoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({
    email: '',
    password: '',
  });

  const navigate = useNavigate();

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setEmail(value);
    setErrors((prev) => ({ ...prev, email: validateEmail(value) }));
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.trim();

    setPassword(value);
    setErrors((prev) => ({ ...prev, password: validatePassword(value) }));
  };

  const isFormValid = () => !errors.email && !errors.password && email && password;

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
    errors,
    handleEmailChange,
    handlePasswordChange,
    isFormValid,
    handleSubmit,
  };
};
