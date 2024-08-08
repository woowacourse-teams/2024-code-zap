import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTagList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { TagListResponse } from '@/types';

export const useTagListQuery = () => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<TagListResponse, Error>({
    queryKey: [QUERY_KEY.TAG_LIST],
    queryFn: () => getTagList({ memberId }),
  });
};
