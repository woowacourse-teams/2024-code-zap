import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';

import type { TemplateUploadRequest } from '@/types';
import { useTemplateUploadMutation } from './useTemplateUploadMutation';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateUploadMutation', () => {
  it('template을 생성할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateUploadMutation(), { wrapper: queryWrapper });

    const body: TemplateUploadRequest = {
      title: 'Upload Test',
      description: '',
      categoryId: 1,
      tags: [],
      snippets: [
        {
          filename: 'filename1.txt',
          content: 'content1',
          ordinal: 1,
        },
      ],
    };

    await result.current.mutateAsync(body);

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
