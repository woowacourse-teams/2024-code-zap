import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api';
import { getLikedTemplateList } from '@/api/templates';
import { TemplateListResponse } from '@/types';

interface Props {
  page: number;
}

export const useLikedTemplateListQuery = ({ page }: Props) =>
  useQuery<TemplateListResponse, Error>({
    queryKey: [QUERY_KEY.LIKED_TEMPLATE, page],
    queryFn: () => getLikedTemplateList({ page }),
    throwOnError: true,
  });
