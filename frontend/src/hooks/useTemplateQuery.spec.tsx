import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { useTemplateQuery } from './useTemplateQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateQuery', () => {
  it('한 개의 template을 조회할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateQuery(2024), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.id).toBe(2024);
      expect(result.current.data?.title).toBe('회원가입 유효성 검증');
    });
  });
});
