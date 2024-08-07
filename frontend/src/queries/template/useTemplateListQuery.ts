import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, getTemplateList } from '@/api';
import { useAuth } from '@/hooks/authentication/useAuth';
import type { TemplateListResponse } from '@/types';

interface Props {
  categoryId?: number;
  tagIds?: number[];
  page?: number;
  pageSize?: number;
  keyword?: string;
}

export const useTemplateListQuery = ({ categoryId, tagIds, page = 1, pageSize = PAGE_SIZE, keyword }: Props) => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, categoryId, tagIds, page, pageSize, keyword, memberId],
    queryFn: () => getTemplateList({ categoryId, tagIds, page, pageSize, keyword, memberId }),
    placeholderData: keepPreviousData,
  });
};
