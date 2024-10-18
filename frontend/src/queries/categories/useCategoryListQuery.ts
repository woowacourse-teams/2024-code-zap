import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getCategoryList } from '@/api';
import type { CategoryListResponse } from '@/types';

interface Props {
  memberId: number;
}

export const useCategoryListQuery = ({ memberId }: Props) => {
  const result = useSuspenseQuery<CategoryListResponse, Error>({
    queryKey: [QUERY_KEY.CATEGORY_LIST, memberId],
    queryFn: () => getCategoryList(memberId),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
