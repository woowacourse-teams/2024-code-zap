import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, deleteTemplate } from '@/api';

export const useTemplateDeleteMutation = (idList: number[]) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => deleteTemplate(idList),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TAG_LIST] });
    },
  });
};
