import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, DEFAULT_SORTING_OPTION, getTemplateExplore } from '@/api';
import { ApiError } from '@/api/Error/ApiError';
import type { SortingKey, TemplateListResponse } from '@/types';

interface Props {
  sort?: SortingKey;
  page?: number;
  size?: number;
  keyword?: string;
}

export const useTemplateExploreQuery = ({
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  keyword,
}: Props) =>
  useQuery<TemplateListResponse, ApiError>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, sort, page, size, keyword],
    queryFn: () => getTemplateExplore({ sort, page, size, keyword }),

    throwOnError: true,
    placeholderData: keepPreviousData,
  });
