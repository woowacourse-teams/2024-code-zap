import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplateList } from '@/api';
import { useAuth } from '@/hooks/authentication';
import { DEFAULT_SORTING_OPTION, PAGE_SIZE } from '@/models/templates';
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
