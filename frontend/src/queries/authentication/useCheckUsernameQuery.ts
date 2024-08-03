import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { checkUsername } from '@/api/authentication';
import { CheckUsernameResponse } from '@/types/authentication';

export const useCheckUsernameQuery = (username: string): UseQueryResult<CheckUsernameResponse, Error> =>
  useQuery<CheckUsernameResponse, Error>({
    queryKey: [QUERY_KEY.CHECK_USERNAME, username],
    queryFn: () => checkUsername(username),
    enabled: false,
  });
