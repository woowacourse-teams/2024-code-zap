import { useCallback, useState } from 'react';

import { DEFAULT_SORTING_OPTION } from '@/api';
import { useDropdown } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useSearchKeyword } from '@/hooks/template';
import { useTemplateListQuery } from '@/queries/templates';
import { scroll } from '@/utils';

const FIRST_PAGE = 1;

interface Props {
  memberId?: number;
}

export const useFilteredTemplateList = ({ memberId: passedMemberId }: Props) => {
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | undefined>(undefined);
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const { keyword, debouncedKeyword, handleKeywordChange } = useSearchKeyword();
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);
  const [page, setPage] = useState<number>(FIRST_PAGE);

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

  const handleCategoryMenuClick = useCallback((selectedCategoryId: number) => {
    setSelectedCategoryId(selectedCategoryId);
    handlePageChange(FIRST_PAGE);
  }, []);

  const handleTagMenuClick = useCallback((selectedTagIds: number[]) => {
    setSelectedTagIds(selectedTagIds);
  }, []);

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handlePageChange(FIRST_PAGE);
    }
  };

  const handlePageChange = (page: number) => {
    scroll.top('smooth');
    setPage(page);
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
