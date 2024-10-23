import { useQueryParams } from '@/hooks';
import { getHotTopicContent } from '@/service/hotTopic';

export const useHotTopic = () => {
  const { queryParams, updateQueryParams } = useQueryParams();
  const selectedTagIds = queryParams.tags;
  const selectedHotTopic = getHotTopicContent(selectedTagIds);

  const selectTopic = ({ tagIds, topic }: { tagIds: number[]; topic: string }) => {
    if (topic === selectedHotTopic) {
      resetSelectedTopic();

      return;
    }

    updateQueryParams({ tags: [...tagIds] });
  };

  const resetSelectedTopic = () => {
    updateQueryParams({ tags: [] });
  };

  return { selectedTagIds, selectedHotTopic, selectTopic };
};
