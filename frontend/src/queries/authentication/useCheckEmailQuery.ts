import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { checkEmail } from '@/api/authentication';
import { CheckEmailResponse } from '@/types/authentication';

export const useCheckEmailQuery = (email: string): UseQueryResult<CheckEmailResponse, Error> =>
  useQuery<CheckEmailResponse, Error>({
    queryKey: [QUERY_KEY.CHECK_EMAIL, email],
    queryFn: () => checkEmail(email),
    enabled: false,
  });
