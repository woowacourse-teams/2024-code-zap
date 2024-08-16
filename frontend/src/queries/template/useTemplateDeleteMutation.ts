import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, deleteTemplate } from '@/api';

export const useTemplateDeleteMutation = (ids: number[]) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => deleteTemplate(ids),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
    },
  });
};
