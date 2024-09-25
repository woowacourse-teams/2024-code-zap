import { useMutation } from '@tanstack/react-query';

import { postLike } from '@/api';

export const useLikeMutation = () =>
  useMutation({
    mutationFn: (templateId: number) => postLike({ templateId }),
  });
