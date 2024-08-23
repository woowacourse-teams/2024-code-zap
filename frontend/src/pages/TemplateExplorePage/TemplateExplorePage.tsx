import { useState } from 'react';
import { Link } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { ArrowUpIcon, ZapzapLogo } from '@/assets/images';
import { Dropdown, Flex, Heading, PagingButtons, TemplateCard } from '@/components';
import { useDropdown, useWindowWidth } from '@/hooks/utils';
import { useTemplateExploreQuery } from '@/queries/template';
import { scroll } from '@/utils';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const [page, setPage] = useState<number>(1);
  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);
  const { data: templateData } = useTemplateExploreQuery({ sort: sortingOption.key, page });
  const windowWidth = useWindowWidth();

  const templates = templateData?.templates || [];
  const totalPages = templateData?.totalPages || 0;

  const handlePageChange = (page: number) => {
    scroll.top();
    setPage(page);
  };

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Flex justify='flex-start' align='center' gap='1rem'>
        <ZapzapLogo width={50} height={50} />
        <Heading.Medium color='black'>여러 템플릿을 구경해보세요:)</Heading.Medium>
      </Flex>
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

      <Flex justify='center' gap='0.5rem' margin='1rem 0' width='100%'>
        <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
      </Flex>

      <S.ScrollTopButton
        onClick={() => {
          scroll.top('smooth');
        }}
      >
        <ArrowUpIcon aria-label='맨 위로' />
      </S.ScrollTopButton>
    </Flex>
  );
};

export default TemplateExplorePage;
