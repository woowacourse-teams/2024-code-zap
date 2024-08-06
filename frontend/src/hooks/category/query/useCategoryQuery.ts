import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getCategoryList } from '@/api';
import { CategoryList } from '@/types/category';

export const useCategoryQuery = (): UseQueryResult<CategoryList, Error> =>
  useQuery<CategoryList, Error>({
    queryKey: [QUERY_KEY.CATEGORY],
    queryFn: () => getCategoryList(),
  });
