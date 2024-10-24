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

    updateQueryParams({ tags: [...tagIds], page: 1 });
  };

  const resetSelectedTopic = () => {
    updateQueryParams({ tags: [], page: 1 });
  };

  return { selectedTagIds, selectedHotTopic, selectTopic };
};
