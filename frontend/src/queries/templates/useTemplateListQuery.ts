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
  size?: number;
  memberId?: number;
}

export const useTemplateListQuery = ({
  keyword,
  categoryId,
  tagIds,
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  memberId: passedMemberId,
}: Props) => {
  const { memberInfo } = useAuth();
  const memberId = passedMemberId ?? memberInfo.memberId;

  return useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, keyword, categoryId, tagIds, sort, page, size, memberId],
    queryFn: () => getTemplateList({ keyword, categoryId, tagIds, sort, page, size, memberId }),
    throwOnError: true,
    placeholderData: keepPreviousData,
  });
};
