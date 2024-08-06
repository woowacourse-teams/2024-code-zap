import { UseQueryResult, useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { checkEmail } from '@/api/authentication';

export const useCheckEmailQuery = (email: string): UseQueryResult =>
  useQuery({
    queryKey: [QUERY_KEY.CHECK_EMAIL, email],
    queryFn: () => checkEmail(email),
    enabled: false,
    retry: false,
  });
