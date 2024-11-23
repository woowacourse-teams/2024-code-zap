import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, DEFAULT_SORTING_OPTION, getTemplateExplore } from '@/api';
import { ApiError } from '@/api/Error';
import type { SortingKey, TemplateListResponse } from '@/types';

interface Props {
  sort?: SortingKey;
  page?: number;
  size?: number;
  keyword?: string;
  tagIds?: number[];
}

export const useTemplateExploreQuery = ({
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  keyword,
  tagIds,
}: Props) =>
  useQuery<TemplateListResponse, ApiError>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, sort, page, size, keyword, tagIds],
    queryFn: () => getTemplateExplore({ sort, page, size, keyword, tagIds }),

    throwOnError: true,
    placeholderData: keepPreviousData,
  });
