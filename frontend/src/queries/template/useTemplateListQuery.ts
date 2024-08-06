import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplateList } from '@/api';
import type { TemplateListResponse } from '@/types';

interface Props {
  categoryId?: number;
  tagId?: number;
  page?: number;
  pageSize?: number;
}

export const useTemplateListQuery = ({ categoryId, tagId, page = 1, pageSize = 20 }: Props) =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, categoryId, tagId, page, pageSize],
    queryFn: () => getTemplateList(categoryId, tagId, page, pageSize),
    placeholderData: keepPreviousData,
  });
