import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, checkName } from '@/api';

export const useCheckNameQuery = (name: string): UseQueryResult =>
  useQuery({
    queryKey: [QUERY_KEY.CHECK_NAME, name],
    queryFn: () => checkName(name),
    enabled: false,
    retry: false,
  });
