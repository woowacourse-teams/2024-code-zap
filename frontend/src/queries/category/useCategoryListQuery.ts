import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getCategoryList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { CategoryListResponse } from '@/types';

export const useCategoryListQuery = () => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<CategoryListResponse, Error>({
    queryKey: [QUERY_KEY.CATEGORY_LIST],
    queryFn: () => getCategoryList({ memberId }),
  });
};
