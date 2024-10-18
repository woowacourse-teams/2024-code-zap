import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplate } from '@/api';
import type { Template } from '@/types';

export const useTemplateQuery = (id: number): UseQueryResult<Template, Error> =>
  useQuery<Template, Error>({
    queryKey: [QUERY_KEY.TEMPLATE, id],
    queryFn: () => getTemplate({ id }),
  });
