import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, deleteTemplate } from '@/api';

export const useTemplateDeleteMutation = (id: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => deleteTemplate(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
    },
  });
};
