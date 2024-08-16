import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';

import { AuthProvider } from '@/contexts';
import { tags as mockTags } from '@/mocks/tagList.json';
import { useTagListQuery } from '../tag/useTagListQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>{children}</AuthProvider>
  </QueryClientProvider>
);

describe('useTagListQuery', () => {
  it('태그 목록을 id 오름차순으로 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useTagListQuery(), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.tags).toEqual(mockTags);
    });
  });
});
