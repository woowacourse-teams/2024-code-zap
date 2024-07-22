import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { useTemplateListQuery } from './useTemplateListQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateListQuery test', () => {
  it('GET  요청시 templates 데이터를 가져온다', async () => {
    const { result } = renderHook(() => useTemplateListQuery(), { wrapper: queryWrapper });

    await waitFor(() => {
      expect(result.current.data?.templates[0].title).toBe('title1');
    });
  });
});
