import { Suspense } from 'react';
import { useParams } from 'react-router-dom';

import { SORTING_OPTIONS } from '@/api';
import { SearchIcon } from '@/assets/images';
import { Flex, Input, PagingButtons, Dropdown, Heading } from '@/components';
import { useAuth } from '@/hooks/authentication';
import { useMemberNameQuery } from '@/queries/members';
import { useTrackPageViewed } from '@/service/amplitude';

import {
  TopBanner,
  CategoryListSection,
  CategoryListSectionSkeleton,
  TagListSection,
  TagListSectionSkeleton,
  TemplateListSection,
  TemplateDeleteSelection,
  TemplateListSectionLoading,
} from './components';
import { useSelectAndDeleteTemplateList, useFilteredTemplateList } from './hooks';
import * as S from './MyTemplatePage.style';

const MyTemplatePage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 내 템플릿 페이지' });

  const { memberId: routeMemberId } = useParams<{ memberId: string }>();
  const memberId = Number(routeMemberId);

  const {
    memberInfo: { memberId: currentMemberId },
  } = useAuth();
  const {
    data: { name },
  } = useMemberNameQuery({ memberId });

  const isMine = memberId === currentMemberId;

  const {
    templateList,
    isTemplateListFetching,
    isTemplateListLoading,
    totalPages,
    dropdownProps,
    keyword,
    searchKeyword,
    page,
    sortingOption,
    selectedTagIds,
    handleKeywordChange,
    handleCategoryMenuClick,
    handleTagMenuClick,
    handleSearchSubmit,
    handlePageChange,
  } = useFilteredTemplateList({ memberId });

  const {
    isEditMode,
    toggleIsEditMode,
    isDeleteModalOpen,
    toggleDeleteModal,
    selectedList,
    setSelectedList,
    handleAllSelected,
    handleDelete,
  } = useSelectAndDeleteTemplateList({ templateList });

  return (
    <S.MyTemplatePageContainer>
      <TopBanner name={name ?? ''} />
      <S.MainContainer>
        <Suspense fallback={<CategoryListSectionSkeleton />}>
          <CategoryListSection memberId={memberId} onSelectCategory={handleCategoryMenuClick} />
        </Suspense>

        <Flex direction='column' width='100%' gap='1rem'>
          {isMine && (
            <TemplateDeleteSelection
              isEditMode={isEditMode}
              isDeleteModalOpen={isDeleteModalOpen}
              selectedListLength={selectedList.length}
              templateListLength={templateList.length}
              toggleIsEditMode={toggleIsEditMode}
              toggleDeleteModal={toggleDeleteModal}
              handleAllSelected={handleAllSelected}
              handleDelete={handleDelete}
            />
          )}

          <Heading.XSmall color='black'>{searchKeyword ? `'${searchKeyword}' 검색 결과` : ''}</Heading.XSmall>

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
            <TagListSection
              memberId={memberId}
              selectedTagIds={selectedTagIds}
              handleTagMenuClick={handleTagMenuClick}
            />
          </Suspense>

          <S.TemplateListSectionWrapper>
            {isTemplateListFetching && <TemplateListSectionLoading />}
            {!isTemplateListLoading && (
              <TemplateListSection
                templateList={templateList}
                isSearching={keyword !== '' || keyword !== searchKeyword}
                isEditMode={isEditMode}
                selectedList={selectedList}
                setSelectedList={setSelectedList}
              />
            )}
          </S.TemplateListSectionWrapper>

          {templateList.length !== 0 && (
            <Flex justify='center' gap='0.5rem' margin='1rem 0'>
              <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
            </Flex>
          )}
        </Flex>
      </S.MainContainer>
    </S.MyTemplatePageContainer>
  );
};

export default MyTemplatePage;
