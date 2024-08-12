import { useState, useCallback } from 'react';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { searchIcon } from '@/assets/images';
import {
  CategoryFilterMenu,
  Flex,
  Heading,
  Input,
  TemplateGrid,
  PagingButton,
  Dropdown,
  TagFilterMenu,
} from '@/components';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useWindowWidth, useDebounce } from '@/hooks/utils';
import { useDropdown } from '@/hooks/utils/useDropdown';
import { useInput } from '@/hooks/utils/useInput';
import { useCategoryListQuery } from '@/queries/category';
import { useTagListQuery } from '@/queries/tag';
import { useTemplateListQuery } from '@/queries/template';
import { theme } from '@/style/theme';
import { scroll } from '@/utils';
import * as S from './MyTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyTemplatePage = () => {
  const windowWidth = useWindowWidth();
  const {
    memberInfo: { username },
  } = useAuth();

  const [keyword, handleKeywordChange] = useInput('');
  const debouncedKeyword = useDebounce(keyword, 300);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | undefined>(undefined);
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);

  const [page, setPage] = useState<number>(1);

  const { data: templateData } = useTemplateListQuery({
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

  return (
    <S.MyTemplatePageContainer>
      <S.TopBannerContainer>
        <S.TopBannerTextWrapper>
          <Heading.Medium color={theme.color.light.black}>{username || '코드잽'}</Heading.Medium>
          <Heading.XSmall color={theme.color.light.black} weight='regular'>
            {'님의 템플릿 입니다 :)'}
          </Heading.XSmall>
        </S.TopBannerTextWrapper>
      </S.TopBannerContainer>
      <S.MainContainer>
        <Flex direction='column' gap='2.5rem' style={{ marginTop: '3.5rem' }}>
          <CategoryFilterMenu categories={categories} onSelectCategory={handleCategoryMenuClick} />
          <TagFilterMenu tags={tags} selectedTagIds={selectedTagIds} onSelectTags={handleTagMenuClick} />
        </Flex>

        <Flex direction='column' width='100%' gap='1rem'>
          <Flex width='100%' gap='1rem'>
            <S.SearchInput size='medium' variant='text'>
              <Input.Adornment>
                <img src={searchIcon} />
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
          <TemplateGrid templates={templates} cols={getGridCols(windowWidth)} />
          <Flex justify='center'>
            {[...Array(totalPages)].map((_, index) => (
              <PagingButton key={index + 1} page={index + 1} isActive={page === index + 1} onClick={handlePageChange} />
            ))}
          </Flex>
        </Flex>
      </S.MainContainer>
    </S.MyTemplatePageContainer>
  );
};

export default MyTemplatePage;
