import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { ArrowUpIcon, PlusIcon, SearchIcon } from '@/assets/images';
import { Flex, Heading, Input, PagingButtons, Dropdown, Button, Modal, Text, NoSearchResults } from '@/components';
import { useWindowWidth, useDebounce, useToggle, useDropdown, useInput } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useTemplateDeleteMutation, useTemplateCategoryTagQueries } from '@/queries/templates';
import { END_POINTS } from '@/routes';
import { theme } from '@/style/theme';
import { scroll } from '@/utils';

import { CategoryFilterMenu, TemplateGrid, TagFilterMenu } from './components';
import * as S from './MyTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyTemplatePage = () => {
  const windowWidth = useWindowWidth();
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

  const [{ data: templateData }, { data: categoryData }, { data: tagData }] = useTemplateCategoryTagQueries({
    keyword: debouncedKeyword,
    categoryId: selectedCategoryId,
    tagIds: selectedTagIds,
    sort: sortingOption.key,
    page,
  });

  const templateList = templateData?.templates || [];
  const categoryList = categoryData?.categories || [];
  const tagList = tagData?.tags || [];
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

  const renderTemplateContent = () => {
    if (templateList.length === 0) {
      if (debouncedKeyword !== '') {
        return <NoSearchResults />;
      } else {
        return <NewTemplateButton />;
      }
    }

    return (
      <TemplateGrid
        templateList={templateList}
        cols={getGridCols(windowWidth)}
        isEditMode={isEditMode}
        selectedList={selectedList}
        setSelectedList={setSelectedList}
      />
    );
  };

  return (
    <S.MyTemplatePageContainer>
      <TopBanner name={name ?? '나'} />
      <S.MainContainer>
        <Flex direction='column' gap='2.5rem' style={{ marginTop: '4.5rem' }}>
          <CategoryFilterMenu categoryList={categoryList} onSelectCategory={handleCategoryMenuClick} />
        </Flex>

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
          {tagList.length !== 0 && (
            <TagFilterMenu tagList={tagList} selectedTagIds={selectedTagIds} onSelectTags={handleTagMenuClick} />
          )}
          {renderTemplateContent()}

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

interface TopBannerProps {
  name: string;
}

const TopBanner = ({ name }: TopBannerProps) => (
  <S.TopBannerContainer>
    <S.TopBannerTextWrapper>
      <Heading.Medium color={theme.color.light.black}>{name}</Heading.Medium>
      <Heading.XSmall color={theme.color.light.black} weight='regular'>
        {`${name ? '님' : ''}의 템플릿 입니다 :)`}
      </Heading.XSmall>
    </S.TopBannerTextWrapper>
  </S.TopBannerContainer>
);

const NewTemplateButton = () => {
  const navigate = useNavigate();

  return (
    <S.NewTemplateButton onClick={() => navigate(END_POINTS.TEMPLATES_UPLOAD)}>
      <PlusIcon width={24} height={24} aria-label='새 템플릿' />
      <Text.Large color={theme.color.light.primary_500} weight='bold'>
        이곳을 눌러 새 템플릿을 추가해보세요 :)
      </Text.Large>
    </S.NewTemplateButton>
  );
};

export default MyTemplatePage;

interface ConfirmDeleteModalProps {
  isDeleteModalOpen: boolean;
  toggleDeleteModal: () => void;
  handleDelete: () => void;
}

const ConfirmDeleteModal = ({ isDeleteModalOpen, toggleDeleteModal, handleDelete }: ConfirmDeleteModalProps) => (
  <Modal isOpen={isDeleteModalOpen} toggleModal={toggleDeleteModal} size='xsmall'>
    <Flex direction='column' justify='space-between' align='center' margin='1rem 0 0 0' gap='2rem'>
      <Flex direction='column' justify='center' align='center' gap='0.75rem'>
        <Text.Large color='black' weight='bold'>
          정말 삭제하시겠습니까?
        </Text.Large>
        <Text.Medium color='black'>삭제된 템플릿은 복구할 수 없습니다.</Text.Medium>
      </Flex>
      <Flex justify='center' align='center' gap='0.5rem'>
        <Button variant='outlined' onClick={toggleDeleteModal}>
          취소
        </Button>
        <Button onClick={handleDelete}>삭제</Button>
      </Flex>
    </Flex>
  </Modal>
);

const ScrollTopButton = () => (
  <S.ScrollTopButton
    onClick={() => {
      scroll.top('smooth');
    }}
  >
    <ArrowUpIcon aria-label='맨 위로' />
  </S.ScrollTopButton>
);
