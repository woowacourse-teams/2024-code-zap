import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getTemplate } from '@/api';
import type { Template } from '@/types';

import { useAuth } from '../../hooks/authentication/useAuth';

export const useTemplateQuery = (id: number): UseQueryResult<Template, Error> => {
  const {
    memberInfo: { memberId },
  } = useAuth();

  return useQuery<Template, Error>({
    queryKey: [QUERY_KEY.TEMPLATE, id, memberId],
    queryFn: () => getTemplate({ id, memberId }),
  });
};
