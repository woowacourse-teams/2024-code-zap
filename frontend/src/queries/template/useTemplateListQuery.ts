import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, getTemplateList } from '@/api';
import type { TemplateListResponse } from '@/types';

interface Props {
  categoryId?: number;
  tagId?: number;
  page?: number;
  pageSize?: number;
  keyword?: string;
}

export const useTemplateListQuery = ({ categoryId, tagId, page = 1, pageSize = PAGE_SIZE, keyword }: Props) =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, categoryId, tagId, page, pageSize, keyword],
    queryFn: () => getTemplateList({ categoryId, tagId, page, pageSize, keyword }),
    placeholderData: keepPreviousData,
  });
