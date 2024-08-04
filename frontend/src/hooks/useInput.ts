import { useState } from 'react';

export const useInput = (initialValue: string, validate?: (value: string, compareValue?: string) => string) => {
  const [value, setValue] = useState(initialValue);
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value.trim();

    setValue(newValue);

    if (validate) {
      setErrorMessage(validate(newValue));
    } else {
      setErrorMessage(newValue);
    }
  };

  return {
    value,
    errorMessage,
    handleChange,
    handleErrorMessage: setErrorMessage,
  };
};
