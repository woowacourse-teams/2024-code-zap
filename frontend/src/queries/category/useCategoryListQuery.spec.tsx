import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';

import { categories as mockCategories } from '@/mocks/categoryList.json';
import { AuthProvider } from '../../contexts/authContext';
import { useCategoryListQuery } from './useCategoryListQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>{children}</AuthProvider>
  </QueryClientProvider>
);

describe('useTagListQuery', () => {
  it('카테고리 목록을 id 오름차순으로 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useCategoryListQuery(), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.categories).toEqual(mockCategories);
    });
  });
});
