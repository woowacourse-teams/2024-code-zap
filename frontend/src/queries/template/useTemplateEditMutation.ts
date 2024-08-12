import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, editTemplate } from '@/api';

export const useTemplateEditMutation = (id: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: editTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE, id] });
    },
  });
};
