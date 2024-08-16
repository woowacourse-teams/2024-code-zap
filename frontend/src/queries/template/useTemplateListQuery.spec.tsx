import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { renderHook, waitFor } from '@testing-library/react';

import { AuthProvider } from '@/contexts';
import { templates as mockTemplates } from '@/mocks/templateList.json';
import { DEFAULT_SORTING_OPTION, PAGE_SIZE } from '../../api/templates';
import { useTemplateListQuery } from './useTemplateListQuery';

const queryClient = new QueryClient();

const queryWrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>{children}</AuthProvider>
  </QueryClientProvider>
);

describe('useTemplateListQuery', () => {
  it('템플릿 리스트를 조회할 수 있다.', async () => {
    const { result } = renderHook(
      () =>
        useTemplateListQuery({
          keyword: '',
          categoryId: undefined,
          tagIds: [],
          sort: DEFAULT_SORTING_OPTION.key,
          page: 1,
          pageSize: PAGE_SIZE,
        }),
      { wrapper: queryWrapper },
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data?.templates.length).toBe(20);
      expect(result.current.data?.templates[0]).toEqual(mockTemplates[0]);
    });
  });
  it('키워드로 템플릿 리스트를 필터링할 수 있다.', async () => {
    const { result } = renderHook(
      () =>
        useTemplateListQuery({
          keyword: 'console.log',
          categoryId: undefined,
          tagIds: [],
          sort: 'modifiedAt,desc',
          page: 1,
          pageSize: 20,
        }),
      { wrapper: queryWrapper },
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data).toBeDefined();
      result.current.data?.templates.forEach((template) => {
        expect(
          template.title.includes('console.log') ||
            template.description.includes('console.log') ||
            template.snippets.some((snippet) => snippet.content.includes('console.log')),
        ).toBe(true);
      });
    });
  });
  it('카테고리로 템플릿 리스트를 필터링할 수 있다.', async () => {
    const { result } = renderHook(
      () =>
        useTemplateListQuery({
          keyword: '',
          categoryId: 1,
          tagIds: [],
          sort: 'modifiedAt,desc',
          page: 1,
          pageSize: 20,
        }),
      { wrapper: queryWrapper },
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data).toBeDefined();
      result.current.data?.templates.forEach((template) => {
        expect(template.category.id).toBe(1);
      });
    });
  });
  it('태그로 템플릿 리스트를 필터링할 수 있다.', async () => {
    const { result } = renderHook(
      () =>
        useTemplateListQuery({
          keyword: '',
          categoryId: undefined,
          tagIds: [3, 5],
          sort: 'modifiedAt,desc',
          page: 1,
          pageSize: 20,
        }),
      { wrapper: queryWrapper },
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data).toBeDefined();
      result.current.data?.templates.forEach((template) => {
        const tagIds = template.tags.map((tag) => tag.id);

        expect(tagIds).toContain(3);
        expect(tagIds).toContain(5);
      });
    });
  });
  it('페이지네이션을 사용할 수 있다.', async () => {
    const { result } = renderHook(
      () =>
        useTemplateListQuery({
          keyword: '',
          categoryId: undefined,
          tagIds: [],
          sort: 'modifiedAt,desc',
          page: 2,
          pageSize: 20,
        }),
      { wrapper: queryWrapper },
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(result.current.data).toBeDefined();
      expect(result.current.data?.templates.length).toBeGreaterThan(0);
    });
  });
});
