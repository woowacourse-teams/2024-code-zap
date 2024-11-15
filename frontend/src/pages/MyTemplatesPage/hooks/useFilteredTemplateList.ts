import { useCallback, useEffect } from 'react';

import { useDropdown, useInput, useQueryParams } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
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
  const [inputKeyword, handleInputKeywordChange] = useInput(queryParams.keyword);

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
    keyword: queryParams.keyword,
    sort: sortingOption.key,
    page,
  });

  const templateList = templateData?.templates || [];
  const paginationSizes = templateData?.paginationSizes || 0;

  useEffect(() => {
    if (queryParams.sort === sortingOption.value) {
      return;
    }

    updateQueryParams({ sort: sortingOption.value, page: FIRST_PAGE });
  }, [queryParams.sort, sortingOption, updateQueryParams]);

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

      updateQueryParams({ keyword: inputKeyword, page: FIRST_PAGE });
    }
  };

  return {
    templateList,
    isTemplateListFetching,
    isTemplateListLoading,
    paginationSizes,
    dropdownProps,
    inputKeyword,
    searchedKeyword: queryParams.keyword,
    page,
    sortingOption,
    selectedTagIds,
    handleKeywordChange: handleInputKeywordChange,
    handleCategoryMenuClick,
    handleTagMenuClick,
    handleSearchSubmit,
    handlePageChange,
  };
};
