import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTagList } from '@/api';
import type { TagListResponse } from '@/types';

interface Props {
  memberId: number;
}

export const useTagListQuery = ({ memberId }: Props) => {
  const result = useSuspenseQuery<TagListResponse, Error>({
    queryKey: [QUERY_KEY.TAG_LIST, memberId],
    queryFn: () => getTagList(memberId),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
