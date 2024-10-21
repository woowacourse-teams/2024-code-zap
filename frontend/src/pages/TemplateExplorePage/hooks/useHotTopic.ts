import { useState } from 'react';

export const useHotTopic = () => {
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const [selectedHotTopic, setSelectedHotTopic] = useState('');

  const selectTopic = ({ tagIds, content }: { tagIds: number[]; content: string }) => {
    if (content === selectedHotTopic) {
      resetSelectedTopic();

      return;
    }

    setSelectedTagIds([...tagIds]);
    setSelectedHotTopic(content);
  };

  const resetSelectedTopic = () => {
    setSelectedTagIds([]);
    setSelectedHotTopic('');
  };

  return { selectedTagIds, selectedHotTopic, selectTopic };
};
