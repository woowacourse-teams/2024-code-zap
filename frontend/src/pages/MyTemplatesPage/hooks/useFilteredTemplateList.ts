import { useCallback, useEffect } from 'react';

import { useDropdown, useQueryParams } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useSearchKeyword } from '@/hooks/template';
import { useTemplateListQuery } from '@/queries/templates';
import { getSortingOptionByValue } from '@/service/getSortingOptionByValue';
import { scroll } from '@/utils';

const FIRST_PAGE = 1;

interface Props {
  memberId?: number;
}

export const useFilteredTemplateList = ({ memberId: passedMemberId }: Props) => {
  const { queryParams, updateQueryParams } = useQueryParams();

  const selectedCategoryId = queryParams.category;
  const selectedTagIds = queryParams.tags;
  const page = queryParams.page;
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(getSortingOptionByValue(queryParams.sort));
  const { keyword, debouncedKeyword, handleKeywordChange } = useSearchKeyword(queryParams.keyword);

  const { memberInfo } = useAuth();
  const memberId = passedMemberId ?? memberInfo.memberId;

  const {
    data: templateData,
    isFetching: isTemplateListFetching,
    isLoading: isTemplateListLoading,
  } = useTemplateListQuery({
    memberId,
    categoryId: selectedCategoryId,
    tagIds: selectedTagIds,
    keyword: debouncedKeyword,
    sort: sortingOption.key,
    page,
  });

  const templateList = templateData?.templates || [];
  const totalPages = templateData?.totalPages || 0;

  useEffect(() => {
    if (queryParams.sort === sortingOption.value) {
      return;
    }

    updateQueryParams({ sort: sortingOption.value, page: FIRST_PAGE });
  }, [queryParams.sort, sortingOption, updateQueryParams]);

  useEffect(() => {
    if (queryParams.keyword === debouncedKeyword) {
      return;
    }

    updateQueryParams({ keyword: debouncedKeyword, page: FIRST_PAGE });
  }, [queryParams.keyword, debouncedKeyword, updateQueryParams]);

  const handlePageChange = (page: number) => {
    scroll.top('smooth');

    updateQueryParams({ page });
  };

  const handleCategoryMenuClick = useCallback(
    (selectedCategoryId: number) => {
      updateQueryParams({ category: selectedCategoryId, page: FIRST_PAGE });
    },
    [updateQueryParams],
  );

  const handleTagMenuClick = useCallback(
    (selectedTagIds: number[]) => {
      updateQueryParams({ tags: selectedTagIds, page: FIRST_PAGE });
    },
    [updateQueryParams],
  );

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      updateQueryParams({ page: FIRST_PAGE });
    }
  };

  return {
    templateList,
    isTemplateListFetching,
    isTemplateListLoading,
    totalPages,
    dropdownProps,
    keyword,
    page,
    sortingOption,
    selectedTagIds,
    handleKeywordChange,
    handleCategoryMenuClick,
    handleTagMenuClick,
    handleSearchSubmit,
    handlePageChange,
  };
};
