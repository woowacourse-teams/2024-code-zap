import { useCallback, useEffect, useState } from 'react';

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

  const [selectedCategoryId, setSelectedCategoryId] = useState<number>(queryParams.category);
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>(queryParams.tags);
  const { keyword, debouncedKeyword, handleKeywordChange } = useSearchKeyword(queryParams.keyword);
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(getSortingOptionByValue(queryParams.sort));
  const [page, setPage] = useState<number>(queryParams.page);

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
    updateQueryParams({ keyword: debouncedKeyword, sort: sortingOption.value, page });
  }, [debouncedKeyword, sortingOption, page, updateQueryParams]);

  const handlePageChange = (page: number) => {
    scroll.top('smooth');

    setPage(page);
  };

  const handleCategoryMenuClick = useCallback(
    (selectedCategoryId: number) => {
      updateQueryParams({ category: selectedCategoryId });

      setSelectedCategoryId(selectedCategoryId);

      handlePageChange(FIRST_PAGE);
    },
    [updateQueryParams],
  );

  const handleTagMenuClick = useCallback(
    (selectedTagIds: number[]) => {
      updateQueryParams({ tags: selectedTagIds });

      setSelectedTagIds(selectedTagIds);
      handlePageChange(FIRST_PAGE);
    },
    [updateQueryParams],
  );

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handlePageChange(FIRST_PAGE);
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
