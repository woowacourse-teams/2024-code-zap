import { Flex, Heading, TemplateCard } from '@/components';
import { useWindowWidth } from '@/hooks/utils';
import { useTemplateExploreQuery } from '@/queries/template';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const { data: templateData } = useTemplateExploreQuery({});
  const windowWidth = useWindowWidth();

  return (
    <Flex direction='column' gap='3rem' align='center' css={{ paddingTop: '10rem' }}>
      <Heading.Large color='black'>구경가기 페이지입니다.</Heading.Large>

      <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
        {templateData?.templates.map((template) => <TemplateCard key={template.id} template={template} />)}
      </S.TemplateExplorePageContainer>
    </Flex>
  );
};

export default TemplateExplorePage;
