import { useState } from 'react';

import { useNoSpaceInput, useScreenReader } from '@/hooks';

export const useTag = (initTags: string[]) => {
  const [tags, setTags] = useState<string[]>(initTags);
  const [value, handleValue, resetValue] = useNoSpaceInput('');
  const { updateScreenReaderMessage } = useScreenReader();

  const addTag = (newTag: string) => {
    if (newTag === '' || tags.includes(newTag)) {
      return;
    }

    setTags((prev) => [...prev, newTag]);
    updateScreenReaderMessage(`${newTag} 태그 등록`);
  };

  const deleteTag = (tag: string) => {
    setTags((prev) => prev.filter((el) => el !== tag));
  };

  return {
    tags,
    addTag,
    deleteTag,
    value,
    handleValue,
    resetValue,
  };
};
