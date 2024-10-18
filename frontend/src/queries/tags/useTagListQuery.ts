import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTagList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { TagListResponse } from '@/types';

export const useTagListQuery = () => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  const result = useSuspenseQuery<TagListResponse, Error>({
    queryKey: [QUERY_KEY.TAG_LIST],
    queryFn: () => getTagList({ memberId }),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
