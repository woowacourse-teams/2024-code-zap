import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';

import { AuthProvider } from '@/contexts';

import { useTemplateQuery } from './useTemplateQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>{children}</AuthProvider>
  </QueryClientProvider>
);

describe('useTemplateQuery', () => {
  it('한 개의 template을 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateQuery(1), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.id).toBe(1);
      expect(result.current.data?.title).toBe('title1');
      expect(result.current.data?.description).toBe('description1');
      expect(result.current.data?.category.id).toBe(2);
      expect(result.current.data?.tags).toEqual([
        { id: 3, name: 'JavaScript' },
        { id: 5, name: 'Backend' },
      ]);
    });
  });
});
