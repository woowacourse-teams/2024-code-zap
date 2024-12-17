import { useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION } from '@/models/templates';

interface FilterState {
  category: number;
  tags: number[];
  keyword: string;
  sort: string;
  page: number;
}

const FIRST_PAGE = 1;

export const useQueryParams = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const getQueryParams = useCallback((): FilterState => {
    const category = searchParams.get('category');
    const tags = searchParams.get('tags');
    const keyword = searchParams.get('keyword');
    const sort = searchParams.get('sort');
    const page = searchParams.get('page');

    return {
      category: category ? Number(category) : 0,
      tags: tags ? JSON.parse(tags) : [],
      keyword: keyword || '',
      sort: sort || DEFAULT_SORTING_OPTION.value,
      page: page ? Number(page) : FIRST_PAGE,
    };
  }, [searchParams]);

  const updateQueryParams = useCallback(
    (updates: Partial<FilterState>) => {
      const current = getQueryParams();
      const newParams = { ...current, ...updates };

      const cleanParams = new URLSearchParams();

      if (newParams.category > 0) {
        cleanParams.append('category', newParams.category.toString());
      }

      if (newParams.tags && newParams.tags.length > 0) {
        cleanParams.append('tags', JSON.stringify(newParams.tags));
      }

      if (newParams.keyword) {
        cleanParams.append('keyword', newParams.keyword);
      }

      if (newParams.sort !== DEFAULT_SORTING_OPTION.value) {
        cleanParams.append('sort', newParams.sort);
      }

      if (newParams.page > FIRST_PAGE) {
        cleanParams.append('page', newParams.page.toString());
      }

      setSearchParams(cleanParams);
    },
    [setSearchParams, getQueryParams],
  );

  return {
    queryParams: getQueryParams(),
    updateQueryParams,
  };
};
