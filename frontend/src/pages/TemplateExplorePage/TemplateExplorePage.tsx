import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { ErrorBoundary } from 'react-error-boundary';
import { Link } from 'react-router-dom';

import { SORTING_OPTIONS } from '@/api';
import { ArrowUpIcon, SearchIcon } from '@/assets/images';
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
import { useDebounce, useDropdown, useInput, useQueryParams, useWindowWidth } from '@/hooks';
import { useTemplateExploreQuery } from '@/queries/templates';
import { useTrackPageViewed } from '@/service/amplitude';
import { getSortingOptionByValue } from '@/service/getSortingOptionByValue';
import { SortingOption } from '@/types';
import { scroll } from '@/utils';

import { HotTopicCarousel } from './components';
import { useHotTopic } from './hooks';
import { TemplateListSectionLoading } from '../MyTemplatesPage/components';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 구경가기 페이지' });

  const { queryParams, updateQueryParams } = useQueryParams();

  const [page, setPage] = useState<number>(queryParams.page);
  const [keyword, handleKeywordChange] = useInput(queryParams.keyword);

  const debouncedKeyword = useDebounce(keyword, 300);

  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(getSortingOptionByValue(queryParams.sort));

  const { selectedTagIds, selectedHotTopic, selectTopic } = useHotTopic();

  useEffect(() => {
    updateQueryParams({ keyword: debouncedKeyword, sort: sortingOption.value, page });
  }, [debouncedKeyword, sortingOption, page, updateQueryParams]);

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      setPage(1);
    }
  };

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Flex direction='column' justify='flex-start' gap='1rem' width='100%'>
        <Heading.Medium color='black'>
          🔥 지금 인기있는 토픽 {selectedHotTopic && `- ${selectedHotTopic} 보는 중`}
        </Heading.Medium>
        <HotTopicCarousel selectTopic={selectTopic} selectedHotTopic={selectedHotTopic} />
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
            <TemplateList
              page={page}
              setPage={setPage}
              sortingOption={sortingOption}
              keyword={debouncedKeyword}
              tagIds={selectedTagIds}
            />
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
  tagIds,
}: {
  page: number;
  setPage: (page: number) => void;
  sortingOption: SortingOption;
  keyword: string;
  tagIds: number[];
}) => {
  const {
    data: templateData,
    isPending,
    isFetching,
    isLoading,
  } = useTemplateExploreQuery({
    sort: sortingOption.key,
    page,
    keyword,
    tagIds,
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
        <S.TemplateListSectionWrapper>
          {isFetching && <TemplateListSectionLoading />}
          {!isLoading && (
            <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
              {templateList.map((template) => (
                <Link to={`/templates/${template.id}`} key={template.id}>
                  <TemplateCard template={template} />
                </Link>
              ))}
            </S.TemplateExplorePageContainer>
          )}
        </S.TemplateListSectionWrapper>
      )}

      {templateList.length !== 0 && (
        <Flex justify='center' gap='0.5rem' margin='1rem 0' width='100%'>
          <PagingButtons currentPage={page} totalPages={totalPages} onPageChange={handlePageChange} />
        </Flex>
      )}
    </>
  );
};
