import { useMutation, useQueryClient } from '@tanstack/react-query';

import { QUERY_KEY, postTemplate } from '@/api';

export const useTemplateUploadQuery = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
    },
  });
};
