import { Link } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { Dropdown, Flex, Heading, TemplateCard } from '@/components';
import { useDropdown, useWindowWidth } from '@/hooks/utils';
import { useTemplateExploreQuery } from '@/queries/template';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);
  const { data: templateData } = useTemplateExploreQuery({ sort: sortingOption.key });
  const windowWidth = useWindowWidth();

  const templates = templateData?.templates || [];

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Heading.Large color='black'>구경가기 페이지입니다.</Heading.Large>
      <Flex width='100%' justify='flex-end'>
        {' '}
        <Dropdown
          {...dropdownProps}
          options={SORTING_OPTIONS}
          currentValue={sortingOption}
          getOptionLabel={(option) => option.value}
        />
      </Flex>

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
