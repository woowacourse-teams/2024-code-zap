import { useMutation, useQueryClient } from '@tanstack/react-query';

import { deleteCategory } from '@/api/categories';
import { QUERY_KEY } from '@/api/queryKeys';

export const useCategoryDeleteQuery = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
  });
};
