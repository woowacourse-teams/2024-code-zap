import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY, getLoginState } from '@/api';

export const useLoginStateQuery = (): UseQueryResult =>
  useQuery({
    queryKey: [QUERY_KEY.LOGIN_STATE],
    queryFn: () => getLoginState(),
    retry: false,
    refetchOnWindowFocus: false,
    enabled: false,
  });
