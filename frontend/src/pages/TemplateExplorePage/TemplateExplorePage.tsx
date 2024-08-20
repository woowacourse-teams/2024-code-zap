import { Link } from 'react-router-dom';

import { Flex, Heading, TemplateCard } from '@/components';
import { useWindowWidth } from '@/hooks/utils';
import { useTemplateExploreQuery } from '@/queries/template';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const { data: templateData } = useTemplateExploreQuery({});
  const windowWidth = useWindowWidth();

  const templates = templateData?.templates || [];

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Heading.Large color='black'>구경가기 페이지입니다.</Heading.Large>

      <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
        {templates.map((template) => (
          <Link to={`/templates/${template.id}`} key={template.id}>
            <TemplateCard template={template} />
          </Link>
        ))}
      </S.TemplateExplorePageContainer>
    </Flex>
  );
};

export default TemplateExplorePage;
