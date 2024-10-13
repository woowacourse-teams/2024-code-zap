import { useState, useCallback, Suspense } from 'react';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { SearchIcon } from '@/assets/images';
import { Flex, Input, PagingButtons, Dropdown, Button, ScrollTopButton, LoadingBall } from '@/components';
import { useDebounce, useToggle, useDropdown, useInput } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useTemplateDeleteMutation, useTemplateListQuery } from '@/queries/templates';
import { scroll } from '@/utils';

import {
  TopBanner,
  ConfirmDeleteModal,
  CategoryListSection,
  CategoryListSectionSkeleton,
  TagListSection,
  TagListSectionSkeleton,
  TemplateListSection,
} from './components';
import * as S from './MyTemplatePage.style';

const MyTemplatePage = () => {
  const {
    memberInfo: { name },
  } = useAuth();

  const [isEditMode, toggleIsEditMode] = useToggle();
  const [selectedList, setSelectedList] = useState<number[]>([]);
  const [isDeleteModalOpen, toggleDeleteModal] = useToggle();

  const [keyword, handleKeywordChange] = useInput('');
  const debouncedKeyword = useDebounce(keyword, 300);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | undefined>(undefined);
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);

  const [page, setPage] = useState<number>(1);

  const { data: templateData, isFetching: isTemplateListLoading } = useTemplateListQuery({
    keyword: debouncedKeyword,
    categoryId: selectedCategoryId,
    tagIds: selectedTagIds,
    sort: sortingOption.key,
    page,
  });

  const templateList = templateData?.templates || [];
  const totalPages = templateData?.totalPages || 0;

  const { mutateAsync: deleteTemplates } = useTemplateDeleteMutation(selectedList);

  const handleCategoryMenuClick = useCallback((selectedCategoryId: number) => {
    setSelectedCategoryId(selectedCategoryId);
    setPage(1);
  }, []);

  const handleTagMenuClick = useCallback((selectedTagIds: number[]) => {
    setSelectedTagIds(selectedTagIds);
  }, []);

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      setPage(1);
    }
  };

  const handlePageChange = (page: number) => {
    scroll.top();
    setPage(page);
  };

  const handleAllSelected = () => {
    if (selectedList.length === templateList.length) {
      setSelectedList([]);

      return;
    }

    setSelectedList(templateList.map((template) => template.id));
  };

  const handleDelete = () => {
    deleteTemplates();
    toggleIsEditMode();
    toggleDeleteModal();
  };

  return (
    <S.MyTemplatePageContainer>
      <TopBanner name={name ?? ''} />
      <S.MainContainer>
        <Suspense fallback={<CategoryListSectionSkeleton />}>
          <CategoryListSection onSelectCategory={handleCategoryMenuClick} />
        </Suspense>

        <Flex direction='column' width='100%' gap='1rem'>
          <Flex justify='flex-end'>
            {isEditMode ? (
              <Flex gap='0.25rem'>
                <Button variant='text' size='small' onClick={toggleIsEditMode}>
                  돌아가기
                </Button>
                <Button variant='outlined' size='small' onClick={handleAllSelected}>
                  {selectedList.length === templateList.length ? '전체 해제' : '전체 선택'}
                </Button>
                <Button
                  variant={selectedList.length ? 'contained' : 'text'}
                  size='small'
                  onClick={selectedList.length ? toggleDeleteModal : toggleIsEditMode}
                >
                  {selectedList.length ? '삭제하기' : '취소하기'}
                </Button>
              </Flex>
            ) : (
              <Button variant='text' size='small' onClick={toggleIsEditMode}>
                선택 삭제
              </Button>
            )}
          </Flex>
          <Flex width='100%' gap='1rem'>
            <S.SearchInput size='medium' variant='text'>
              <Input.Adornment>
                <SearchIcon aria-label='' />
              </Input.Adornment>
              <Input.TextField
                placeholder='검색'
                value={keyword}
                onChange={handleKeywordChange}
                onKeyDown={handleSearchSubmit}
              />
            </S.SearchInput>
            <Dropdown
              {...dropdownProps}
              options={SORTING_OPTIONS}
              currentValue={sortingOption}
              getOptionLabel={(option) => option.value}
            />
          </Flex>

          <Suspense fallback={<TagListSectionSkeleton />}>
            <TagListSection selectedTagIds={selectedTagIds} handleTagMenuClick={handleTagMenuClick} />
          </Suspense>
          {isTemplateListLoading ? (
            <LoadingBall />
          ) : (
            <TemplateListSection
              templateList={templateList}
              isSearching={debouncedKeyword !== ''}
              isEditMode={isEditMode}
              selectedList={selectedList}
              setSelectedList={setSelectedList}
            />
          )}

          {templateList.length !== 0 && (
            <Flex justify='center' gap='0.5rem' margin='1rem 0'>
              <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
            </Flex>
          )}
        </Flex>

        {isDeleteModalOpen && (
          <ConfirmDeleteModal
            isDeleteModalOpen={isDeleteModalOpen}
            toggleDeleteModal={toggleDeleteModal}
            handleDelete={handleDelete}
          />
        )}
      </S.MainContainer>

      <ScrollTopButton />
    </S.MyTemplatePageContainer>
  );
};

export default MyTemplatePage;
