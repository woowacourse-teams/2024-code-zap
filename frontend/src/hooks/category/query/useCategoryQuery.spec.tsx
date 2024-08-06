import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { PropsWithChildren } from 'react';

import { useCategoryQuery } from './useCategoryQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: PropsWithChildren) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useCategoryQuery', () => {
  it('category를 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useCategoryQuery(), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.categories[0].name).toBe('카테고리 없음');
    });
  });
});
