import { useState } from 'react';

export const useNoSpaceInput = (initValue: string = '') => {
  const [value, setValue] = useState(initValue);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const noSpaceValue = e.target.value.replace(/[\s\u3164\uFEFF\u200B]+/gu, '');

    setValue(noSpaceValue);
  };

  const resetValue = () => {
    setValue('');
  };

  return [value, handleChange, resetValue] as const;
};
