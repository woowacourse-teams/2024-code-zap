import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getCategoryList } from '@/api';
import type { CategoryListResponse } from '@/types';

export const useCategoryListQuery = () =>
  useQuery<CategoryListResponse, Error>({
    queryKey: [QUERY_KEY.CATEGORY_LIST],
    queryFn: getCategoryList,
  });
