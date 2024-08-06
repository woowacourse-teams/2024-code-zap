import { useState, useCallback } from 'react';

import { searchIcon } from '@/assets/images';
import { CategoryMenu, Flex, Heading, Input, TemplateGrid, PagingButton } from '@/components';
import { useWindowWidth } from '@/hooks';
import { useCategoryListQuery } from '@/queries/category';
import { useTemplateListQuery } from '@/queries/template';
import { theme } from '@/style/theme';
import { scroll } from '@/utils';
import * as S from './MyTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyTemplatePage = () => {
  const windowWidth = useWindowWidth();
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | undefined>(undefined);
  const [page, setPage] = useState<number>(1);
  const [pageSize] = useState<number>(20);

  const { data: templateData } = useTemplateListQuery({
    categoryId: selectedCategoryId,
    page,
    pageSize,
  });
  const { data: categoryData } = useCategoryListQuery();

  const handleCategorySelect = useCallback((categoryId: number) => {
    scroll.top();
    setSelectedCategoryId(categoryId);
    setPage(1);
  }, []);

  const templates = templateData?.templates || [];
  const categories = categoryData?.categories || [];
  const totalPages = templateData?.totalPages || 0;

  const handlePageChange = (page: number) => {
    scroll.top();
    setPage(page);
  };

  return (
    <S.MyTemplatePageContainer>
      <S.TopBannerContainer>
        <S.TopBannerTextWrapper>
          <Heading.Medium color={theme.color.light.black}>{'코드잽'}</Heading.Medium>
          <Heading.XSmall color={theme.color.light.black} weight='regular'>
            {'님의 템플릿 입니다 :)'}
          </Heading.XSmall>
        </S.TopBannerTextWrapper>
      </S.TopBannerContainer>
      <S.MainContainer>
        <Flex style={{ marginTop: '72px' }}>
          <CategoryMenu categories={categories} onSelectCategory={handleCategorySelect} />
        </Flex>

        <Flex direction='column' width='100%' gap='2rem'>
          <Flex width='100%'>
            <S.SearchInput size='medium' variant='text'>
              <Input.Adornment>
                <img src={searchIcon} />
              </Input.Adornment>
              <Input.TextField placeholder='검색' />
            </S.SearchInput>
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
