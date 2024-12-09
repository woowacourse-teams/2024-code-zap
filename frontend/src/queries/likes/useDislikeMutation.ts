import { useMutation } from '@tanstack/react-query';

import { deleteLike } from '@/api';

export const useDislikeMutation = () =>
  useMutation({
    mutationFn: (templateId: number) => deleteLike(templateId),
  });
