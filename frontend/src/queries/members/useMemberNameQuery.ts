import { useSuspenseQuery } from '@tanstack/react-query';

import { QUERY_KEY, getMemberName } from '@/api';
import { GetMemberNameResponse } from '@/types';

interface Props {
  memberId: number;
}

export const useMemberNameQuery = ({ memberId }: Props) => {
  const result = useSuspenseQuery<GetMemberNameResponse, Error>({
    queryKey: [QUERY_KEY.MEMBER_NAME, memberId],
    queryFn: () => getMemberName(memberId),
  });

  if (result.error && !result.isFetching) {
    throw result.error;
  }

  return result;
};
