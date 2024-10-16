import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { useState } from 'react';
import { ErrorBoundary } from 'react-error-boundary';
import { Link } from 'react-router-dom';

import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';
import { ArrowUpIcon, SearchIcon, ZapzapLogo } from '@/assets/images';
import {
  Dropdown,
  Flex,
  Heading,
  Input,
  LoadingBall,
  NoSearchResults,
  PagingButtons,
  TemporaryError,
  TemplateCard,
} from '@/components';
import { useDebounce, useDropdown, useInput, useWindowWidth } from '@/hooks';
import { useTemplateExploreQuery } from '@/queries/templates';
import { SortingOption } from '@/types';
import { scroll } from '@/utils';

import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const [page, setPage] = useState<number>(1);
  const [keyword, handleKeywordChange] = useInput('');

  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      setPage(1);
    }
  };

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Flex justify='flex-start' align='center' gap='1rem'>
        <ZapzapLogo width={50} height={50} />
        <Heading.Medium color='black'>여러 템플릿을 구경해보세요:)</Heading.Medium>
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
      <QueryErrorResetBoundary>
        {({ reset }) => (
          <ErrorBoundary
            FallbackComponent={(fallbackProps) => <TemporaryError {...fallbackProps} />}
            onReset={reset}
            resetKeys={[keyword]}
          >
            <TemplateList page={page} setPage={setPage} sortingOption={sortingOption} keyword={keyword} />
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>

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

const TemplateList = ({
  page,
  setPage,
  sortingOption,
  keyword,
}: {
  page: number;
  setPage: (page: number) => void;
  sortingOption: SortingOption;
  keyword: string;
}) => {
  const debouncedKeyword = useDebounce(keyword, 300);

  const { data: templateData, isPending } = useTemplateExploreQuery({
    sort: sortingOption.key,
    page,
    keyword: debouncedKeyword,
  });
  const templateList = templateData?.templates || [];
  const totalPages = templateData?.totalPages || 0;

  const windowWidth = useWindowWidth();

  const handlePageChange = (page: number) => {
    scroll.top();
    setPage(page);
  };

  return (
    <>
      {templateList.length === 0 ? (
        isPending ? (
          <LoadingBall />
        ) : (
          <NoSearchResults />
        )
      ) : (
        <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
          {templateList.map((template) => (
            <Link to={`/templates/${template.id}`} key={template.id}>
              <TemplateCard template={template} />
            </Link>
          ))}
        </S.TemplateExplorePageContainer>
      )}

      {templateList.length !== 0 && (
        <Flex justify='center' gap='0.5rem' margin='1rem 0' width='100%'>
          <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
        </Flex>
      )}
    </>
  );
};
