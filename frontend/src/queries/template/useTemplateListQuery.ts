import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplateList } from '@/api';
import type { TemplateListResponse } from '@/types';

interface UseTemplateListQueryProps {
  categoryId?: number;
  tagId?: number;
}

export const useTemplateListQuery = ({ categoryId, tagId }: UseTemplateListQueryProps) =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST, categoryId, tagId],
    queryFn: () => getTemplateList(categoryId, tagId),
  });
