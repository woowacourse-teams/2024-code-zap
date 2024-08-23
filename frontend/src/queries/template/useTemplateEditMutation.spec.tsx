import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { PropsWithChildren } from 'react';

import type { TemplateEditRequest } from '@/types';
import { useTemplateEditMutation } from './useTemplateEditMutation';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: PropsWithChildren) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateEditMutation', () => {
  it('templates울 수정할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateEditMutation(2024), { wrapper: queryWrapper });

    const template: TemplateEditRequest = {
      title: 'editTemplate',
      description: '',
      createSourceCodes: [],
      updateSourceCodes: [
        {
          filename: 'filename1.txt',
          content: 'content edit',
          ordinal: 1,
        },
      ],
      deleteSourceCodeIds: [],
      categoryId: 1,
      tags: [],
    };

    await result.current.mutateAsync({ id: 2024, template });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
