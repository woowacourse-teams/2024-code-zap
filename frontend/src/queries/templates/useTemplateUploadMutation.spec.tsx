import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';

import { ToastProvider } from '@/contexts';
import type { TemplateUploadRequest } from '@/types';

import { useTemplateUploadMutation } from './useTemplateUploadMutation';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <MemoryRouter>
    <ToastProvider>
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    </ToastProvider>
  </MemoryRouter>
);

describe('useTemplateUploadMutation', () => {
  it('template을 생성할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateUploadMutation(), {
      wrapper: queryWrapper,
    });

    const body: TemplateUploadRequest = {
      title: 'Upload Test',
      description: '',
      categoryId: 1,
      tags: [],
      thumbnailOrdinal: 1,
      sourceCodes: [
        {
          filename: 'filename1.txt',
          content: 'content1',
          ordinal: 1,
        },
      ],
      visibility: 'PUBLIC',
    };

    await result.current.mutateAsync(body);

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
