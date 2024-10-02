import { useState } from 'react';

export const useNoSpaceInput = (initValue: string = '') => {
  const [value, setValue] = useState(initValue);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const noSpaceValue = e.target.value.replace(/[\s\u3000\u115F\u1160\u2800\u3164\uFFA0\u200B\uFEFF]+/gu, '');

    setValue(noSpaceValue);
  };

  const resetValue = () => {
    setValue('');
  };

  return [value, handleChange, resetValue] as const;
};
