import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { checkUsername } from '@/api/authentication';

export const useCheckUsernameQuery = (username: string): UseQueryResult =>
  useQuery({
    queryKey: [QUERY_KEY.CHECK_USERNAME, username],
    queryFn: () => checkUsername(username),
    enabled: false,
    retry: false,
  });
