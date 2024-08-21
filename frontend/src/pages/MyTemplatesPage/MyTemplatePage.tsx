import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { ArrowUpIcon, PlusIcon, SearchIcon } from '@/assets/images';
import {
  CategoryFilterMenu,
  Flex,
  Heading,
  Input,
  TemplateGrid,
  PagingButtons,
  Dropdown,
  TagFilterMenu,
  Button,
  Modal,
  Text,
  LoadingBall,
} from '@/components';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useWindowWidth, useDebounce, useToggle } from '@/hooks/utils';
import { useDropdown } from '@/hooks/utils/useDropdown';
import { useInput } from '@/hooks/utils/useInput';
import { useCategoryListQuery } from '@/queries/category';
import { useTagListQuery } from '@/queries/tag';
import { useTemplateDeleteMutation, useTemplateListQuery } from '@/queries/template';
import { theme } from '@/style/theme';
import { scroll } from '@/utils';
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

  const { data: templateData, isPending } = useTemplateListQuery({
    keyword: debouncedKeyword,
    categoryId: selectedCategoryId,
    tagIds: selectedTagIds,
    sort: sortingOption.key,
    page,
  });
  const { data: categoryData } = useCategoryListQuery();
  const { data: tagData } = useTagListQuery();
  const templates = templateData?.templates || [];
  const categories = categoryData?.categories || [];
  const tags = tagData?.tags || [];
  const totalPages = templateData?.totalPages || 0;

  const { mutateAsync: deleteTemplates } = useTemplateDeleteMutation(selectedList);

  const handleCategoryMenuClick = useCallback((selectedCategoryId: number) => {
    scroll.top();
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
    if (selectedList.length === templates.length) {
      setSelectedList([]);

      return;
    }

    setSelectedList(templates.map((template) => template.id));
  };

  const handleDelete = () => {
    deleteTemplates();
    toggleIsEditMode();
    toggleDeleteModal();
  };

  const renderTemplateContent = () => {
    if (isPending) {
      return <LoadingBall />;
    }

    if (templates.length === 0) {
      if (debouncedKeyword !== '') {
        return <Text.Large color={theme.color.light.secondary_700}>검색 결과가 없습니다.</Text.Large>;
      } else {
        return <NewTemplateButton />;
      }
    }

    return (
      <TemplateGrid
        templates={templates}
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
          <CategoryFilterMenu categories={categories} onSelectCategory={handleCategoryMenuClick} />
        </Flex>

        <Flex direction='column' width='100%' gap='1rem'>
          <Flex justify='flex-end'>
            {isEditMode ? (
              <Flex gap='0.25rem'>
                <Button variant='text' size='small' onClick={toggleIsEditMode}>
                  돌아가기
                </Button>
                <Button variant='outlined' size='small' onClick={handleAllSelected}>
                  {selectedList.length === templates.length ? '전체 해제' : '전체 선택'}
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
          {tags.length && (
            <TagFilterMenu tags={tags} selectedTagIds={selectedTagIds} onSelectTags={handleTagMenuClick} />
          )}
          {renderTemplateContent()}

          {templates.length !== 0 && (
            <Flex justify='center' gap='0.5rem' margin='1rem 0'>
              <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
            </Flex>
          )}
        </Flex>

        {isDeleteModalOpen && (
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
        )}
      </S.MainContainer>

      <S.ScrollTopButton
        onClick={() => {
          scroll.top('smooth');
        }}
      >
        <ArrowUpIcon aria-label='맨 위로' />
      </S.ScrollTopButton>
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
    <S.NewTemplateButton onClick={() => navigate('/templates/upload')}>
      <PlusIcon width={24} height={24} aria-label='새 템플릿' />
      <Text.Large color={theme.color.light.primary_500} weight='bold'>
        이곳을 눌러 새 템플릿을 추가해보세요 :)
      </Text.Large>
    </S.NewTemplateButton>
  );
};

export default MyTemplatePage;
