import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, getTemplateList, DEFAULT_SORTING_OPTION } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { TemplateListResponse, SortingKey } from '@/types';

interface Props {
  keyword?: string;
  categoryId?: number;
  tagIds?: number[];
  sort?: SortingKey;
  page?: number;
  pageSize?: number;
}

export const useTemplateListQuery = ({
  keyword,
  categoryId,
  tagIds,
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  pageSize = PAGE_SIZE,
}: Props) => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, keyword, categoryId, tagIds, sort, page, pageSize, memberId],
    queryFn: () => getTemplateList({ keyword, categoryId, tagIds, sort, page, pageSize, memberId }),
    throwOnError: true,
    placeholderData: keepPreviousData,
  });
};
