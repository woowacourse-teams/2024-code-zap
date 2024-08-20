import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, postCategory } from '@/api';

export const useCategoryUpload = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
  });
};
