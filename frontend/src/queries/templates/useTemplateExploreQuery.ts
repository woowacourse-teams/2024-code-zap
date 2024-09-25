import { keepPreviousData, useQuery } from '@tanstack/react-query';

import { PAGE_SIZE, QUERY_KEY, DEFAULT_SORTING_OPTION, getTemplateExplore } from '@/api';
import type { TemplateListResponse, SortingKey } from '@/types';

import { useAuth } from '../../hooks/authentication/useAuth';

interface Props {
  sort?: SortingKey;
  page?: number;
  size?: number;
}

export const useTemplateExploreQuery = ({ sort = DEFAULT_SORTING_OPTION.key, page = 1, size = PAGE_SIZE }: Props) => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, sort, page, size, memberId],
    queryFn: () => getTemplateExplore({ sort, page, size, memberId }),
    throwOnError: true,
    placeholderData: keepPreviousData,
  });
};
