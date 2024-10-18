import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getCategoryList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { CategoryListResponse } from '@/types';

interface Props {
  memberId?: number;
}

export const useCategoryListQuery = ({ memberId: passedMemberId }: Props) => {
  const { memberInfo } = useAuth();
  const memberId = passedMemberId ?? memberInfo.memberId;

  const result = useSuspenseQuery<CategoryListResponse, Error>({
    queryKey: [QUERY_KEY.CATEGORY_LIST, memberId],
    queryFn: () => getCategoryList({ memberId }),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
