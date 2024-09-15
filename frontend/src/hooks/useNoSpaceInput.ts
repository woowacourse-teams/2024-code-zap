import { useState } from 'react';

export const useNoSpaceInput = (initValue: string = '') => {
  const [value, setValue] = useState(initValue);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => setValue(e.target.value.trim());

  const resetValue = () => {
    setValue('');
  };

  return [value, handleChange, resetValue] as const;
};
