import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY } from '@/api/queryKeys';
import { editTemplate } from '@/api/templates';

export const useTemplateEditQuery = (id: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: editTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE, id] });
    },
  });
};
