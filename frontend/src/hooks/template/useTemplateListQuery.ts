import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api/queryKeys';
import { getTemplateList } from '@/api/templates';
import type { TemplateListResponse } from '@/types/template';

export const useTemplateListQuery = () =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST],
    queryFn: getTemplateList,
  });
