import { useState } from 'react';

import { useNoSpaceInput } from '@/hooks';

export const useTag = (initTags: string[]) => {
  const [tags, setTags] = useState<string[]>(initTags);
  const [value, handleValue, resetValue] = useNoSpaceInput('');

  return {
    tags,
    setTags,
    value,
    handleValue,
    resetValue,
  };
};
