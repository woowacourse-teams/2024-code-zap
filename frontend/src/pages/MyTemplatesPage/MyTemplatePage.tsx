import { searchIcon } from '@/assets/images';
import { CategoryMenu, Flex, Heading, Input, TemplateGrid } from '@/components';
import { useWindowWidth } from '@/hooks/';
import { useTemplateListQuery } from '@/hooks/template';
import categoryList from '@/mocks/categoryList.json';
import { theme } from '@/style/theme';
import * as S from './MyTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyTemplatePage = () => {
  const windowWidth = useWindowWidth();

  const { data, error, isLoading } = useTemplateListQuery();

  if (isLoading) {
    return <div>Loading...</div>;
  } else if (error) {
    return <div>Error: {error.message}</div>;
  }

  const templates = data?.templates || [];

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
          <CategoryMenu categories={categoryList.categories} />
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
