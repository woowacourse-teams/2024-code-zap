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

    const editedTemplate: TemplateEditRequest = {
      id: 2024,
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
      visibility: 'PUBLIC',
    };

    await result.current.mutateAsync(editedTemplate);

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
