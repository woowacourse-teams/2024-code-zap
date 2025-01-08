import { Suspense } from 'react';
import { useParams } from 'react-router-dom';

import { SearchIcon } from '@/assets/images';
import { Flex, Input, PagingButtons, Dropdown, Heading } from '@/components';
import { useAuth } from '@/hooks/authentication';
import { SORTING_OPTIONS } from '@/models/templates';
import {
  CategoryListSection,
  CategoryListSectionSkeleton,
  TagListSection,
  TagListSectionSkeleton,
  TemplateDeleteSelection,
  TemplateListSection,
  TemplateListSectionLoading,
  TopBanner,
} from '@/pages/MemberTemplatePage/components';
import { useFilteredTemplateList, useSelectAndDeleteTemplateList } from '@/pages/MemberTemplatePage/hooks';
import { useMemberNameQuery } from '@/queries/members';
import { useTrackPageViewed } from '@/service/amplitude';

import * as S from './MemberTemplatePage.style';

const MemberTemplatePage = () => {
  const { memberId: routeMemberId } = useParams<{ memberId: string }>();
  const memberId = Number(routeMemberId);

  useTrackPageViewed({ eventName: `[Viewed] 맴버 (ID:${memberId}) 템플릿 페이지` });

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
    paginationSizes,
    dropdownProps,
    inputKeyword,
    searchedKeyword,
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
    <S.MemberTemplatePageContainer>
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

          <S.SearchKeywordPlaceholder>
            <Heading.XSmall color='black'>{searchedKeyword ? `'${searchedKeyword}' 검색 결과` : ''}</Heading.XSmall>
          </S.SearchKeywordPlaceholder>

          <Flex width='100%' gap='1rem'>
            <S.SearchInput size='medium' variant='text'>
              <Input.Adornment>
                <SearchIcon aria-label='' />
              </Input.Adornment>
              <Input.TextField
                placeholder='검색'
                value={inputKeyword}
                onChange={handleKeywordChange}
                onKeyDown={handleSearchSubmit}
              />
            </S.SearchInput>
            <Dropdown
              {...dropdownProps}
              options={[...SORTING_OPTIONS]}
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
                isSearching={inputKeyword !== '' || inputKeyword !== searchedKeyword}
                isEditMode={isEditMode}
                isMine={isMine}
                selectedList={selectedList}
                setSelectedList={setSelectedList}
              />
            )}
          </S.TemplateListSectionWrapper>

          {templateList.length !== 0 && (
            <Flex justify='center' gap='0.5rem' margin='1rem 0'>
              <PagingButtons currentPage={page} paginationSizes={paginationSizes} onPageChange={handlePageChange} />
            </Flex>
          )}
        </Flex>
      </S.MainContainer>
    </S.MemberTemplatePageContainer>
  );
};

export default MemberTemplatePage;