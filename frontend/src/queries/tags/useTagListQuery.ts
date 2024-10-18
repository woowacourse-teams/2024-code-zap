import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTagList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { TagListResponse } from '@/types';

interface Props {
  memberId?: number;
}

export const useTagListQuery = ({ memberId: passedMemberId }: Props) => {
  const { memberInfo } = useAuth();
  const memberId = passedMemberId ?? memberInfo.memberId;

  const result = useSuspenseQuery<TagListResponse, Error>({
    queryKey: [QUERY_KEY.TAG_LIST, memberId],
    queryFn: () => getTagList({ memberId }),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
