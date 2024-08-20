import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, postCategory } from '@/api';
import { Category } from '@/types';

export const useCategoryUploadMutation = (handleCurrentCategory: (newValue: Category) => void = () => {}) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postCategory,
    onSuccess: (res) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
      if ('name' in res) {
        handleCurrentCategory(res);
      }
    },
  });
};
