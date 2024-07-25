import { useMutation, useQueryClient } from '@tanstack/react-query';
import { QUERY_KEY } from '@/api/queryKeys';
import { deleteTemplate } from '@/api/templates';

export const useTemplateDeleteQuery = (id: number) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => deleteTemplate(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
    },
  });
};
