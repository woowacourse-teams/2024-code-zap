import { useMutation, useQueryClient } from '@tanstack/react-query';

import { editCategory } from '@/api/categories';
import { QUERY_KEY } from '@/api/queryKeys';

export const useCategoryEditMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: editCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
  });
};
