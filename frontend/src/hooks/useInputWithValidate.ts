import { useCallback, useState } from 'react';

export const useInputWithValidate = (
  initialValue: string,
  validate?: (value: string, compareValue?: string) => string,
) => {
  const [value, setValue] = useState(initialValue);
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>, compareValue?: string) => {
      const newValue = e.target.value.trim();

      setValue(newValue);

      if (validate) {
        setErrorMessage(validate(newValue, compareValue));
      } else {
        setErrorMessage(newValue);
      }
    },
    [validate],
  );

  return {
    value,
    errorMessage,
    handleChange,
    handleErrorMessage: setErrorMessage,
  };
};
