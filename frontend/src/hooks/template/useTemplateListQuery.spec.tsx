import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { PropsWithChildren } from 'react';

import { useTemplateListQuery } from './useTemplateListQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: PropsWithChildren) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateListQuery', () => {
  it('templates 목록을 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateListQuery(), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.templates[0].title).toBe('title1');
    });
  });
});
