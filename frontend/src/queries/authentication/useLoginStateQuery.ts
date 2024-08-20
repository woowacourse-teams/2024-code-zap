import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { getLoginState } from '@/api/authentication';

export const useLoginStateQuery = (): UseQueryResult =>
  useQuery({
    queryKey: [QUERY_KEY.LOGIN_STATE],
    queryFn: () => getLoginState(),
    retry: false,
    refetchOnWindowFocus: false,
    enabled: false,
  });
