import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';
import { PropsWithChildren } from 'react';

import { useTemplateDeleteMutation } from './useTemplateDeleteMutation';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: PropsWithChildren) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

describe('useTemplateDeleteMutation', () => {
  it('templates을 삭제할 수 있다.', async () => {
    const { result } = renderHook(() => useTemplateDeleteMutation(2024), { wrapper: queryWrapper });

    await result.current.mutateAsync();

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
