import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, postCategory } from '@/api';

<<<<<<<< HEAD:frontend/src/queries/category/useCategoryUploadQuery.ts
export const useCategoryUploadQuery = () => {
========
export const useCategoryUploadMutation = () => {
>>>>>>>> 04d5676b0b53a72ab3effdbd93871ed180426fc1:frontend/src/queries/category/useCategoryUploadMutation.ts
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
  });
};
