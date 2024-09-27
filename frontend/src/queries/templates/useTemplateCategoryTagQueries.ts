import { useSuspenseQueries } from '@tanstack/react-query';

import { QUERY_KEY, getTemplateList, getTagList, getCategoryList, DEFAULT_SORTING_OPTION, PAGE_SIZE } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { SortingKey } from '@/types';

interface Props {
  keyword?: string;
  categoryId?: number;
  tagIds?: number[];
  sort?: SortingKey;
  page?: number;
  size?: number;
}

export const useTemplateCategoryTagQueries = ({
  keyword,
  categoryId,
  tagIds,
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
}: Props) => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useSuspenseQueries({
    queries: [
      {
        queryKey: [QUERY_KEY.TEMPLATE_LIST, keyword, categoryId, tagIds, sort, page, size, memberId],
        queryFn: () => getTemplateList({ keyword, categoryId, tagIds, sort, page, size, memberId }),
      },
      {
        queryKey: [QUERY_KEY.CATEGORY_LIST],
        queryFn: () => getCategoryList({ memberId }),
      },
      {
        queryKey: [QUERY_KEY.TAG_LIST],
        queryFn: () => getTagList({ memberId }),
      },
    ],
  });
};
