import { useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, editTemplate } from '@/api';
import { usePreventDuplicateMutation } from '@/hooks';

export const useTemplateEditMutation = (id: number) => {
  const queryClient = useQueryClient();

  return usePreventDuplicateMutation({
    mutationFn: editTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE, id] });
    },
  });
};
