import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, editCategory } from '@/api';

export const useCategoryEditMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: editCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
  });
};
