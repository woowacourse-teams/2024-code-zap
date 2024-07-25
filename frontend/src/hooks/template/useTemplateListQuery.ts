import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplateList } from '@/api';
import type { TemplateListResponse } from '@/types/template';

export const useTemplateListQuery = () =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.TEMPLATE_LIST],
    queryFn: getTemplateList,
  });
