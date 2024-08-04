import { searchIcon } from '@/assets/images';
import { CategoryMenu, Flex, Heading, Input, TemplateGrid } from '@/components';
import { useWindowWidth } from '@/hooks';
import { useTemplateListQuery } from '@/hooks/template';
import { useCategoryListQuery } from '@/queries/category';
import { theme } from '@/style/theme';
import * as S from './MyTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyTemplatePage = () => {
  const windowWidth = useWindowWidth();

  const { data: templateData, error: templateError, isLoading: templateLoading } = useTemplateListQuery();
  const { data: categoryData, error: categoryError, isLoading: categoryLoading } = useCategoryListQuery();

  if (templateLoading || categoryLoading) {
    return <div>Loading...</div>;
  } else if (templateError) {
    return <div>Error: {templateError.message}</div>;
  } else if (categoryError) {
    return <div>Error: {categoryError.message}</div>;
  }

  const templates = templateData?.templates || [];
  const categories = categoryData?.categories || [];

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
          <CategoryMenu categories={categories} />
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
        </Flex>
      </S.MainContainer>
    </S.MyTemplatePageContainer>
  );
};

export default MyTemplatePage;
