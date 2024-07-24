import { UseQueryResult, useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from '@/api/queryKeys';
import { getTemplate } from '@/api/templates';
import { Template } from '@/types/template';

export const useTemplateQuery = (id: number): UseQueryResult<Template, Error> =>
  useQuery<Template, Error>({
    queryKey: [QUERY_KEY.TEMPLATE, id],
    queryFn: () => getTemplate(id),
  });
