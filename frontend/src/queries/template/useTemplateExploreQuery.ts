import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, DEFAULT_SORTING_OPTION } from '@/api';
import { getTemplateExplore } from '@/api/templates';
import type { TemplateListResponse, SortingKey } from '@/types';

interface Props {
  sort?: SortingKey;
  page?: number;
  pageSize?: number;
}

export const useTemplateExploreQuery = ({ sort = DEFAULT_SORTING_OPTION.key, page = 1, pageSize = PAGE_SIZE }: Props) =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, sort, page, pageSize],
    queryFn: () => getTemplateExplore({ sort, page, pageSize }),
    throwOnError: true,
    placeholderData: keepPreviousData,
  });