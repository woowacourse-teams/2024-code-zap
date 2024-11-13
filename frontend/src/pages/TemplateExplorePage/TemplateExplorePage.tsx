import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { useEffect } from 'react';
import { ErrorBoundary } from 'react-error-boundary';

import { SORTING_OPTIONS } from '@/api';
import { SearchIcon } from '@/assets/images';
import {
  Dropdown,
  Flex,
  Heading,
  Input,
  LoadingBall,
  NoResults,
  PagingButtons,
  TemporaryError,
  TemplateCard,
} from '@/components';
import { useDebounce, useDropdown, useInput, useQueryParams, useWindowWidth } from '@/hooks';
import { useTemplateExploreQuery } from '@/queries/templates';
import { useTrackPageViewed } from '@/service/amplitude';
import { getSortingOptionByValue } from '@/service/getSortingOptionByValue';
import { BREAKING_POINT } from '@/style/styleConstants';
import { SortingOption } from '@/types';
import { scroll } from '@/utils';

import { HotTopicCarousel } from './components';
import { useHotTopic } from './hooks';
import { TemplateListSectionLoading } from '../MyTemplatesPage/components';
import * as S from './TemplateExplorePage.style';

const FIRST_PAGE = 1;

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 구경가기 페이지' });

  const windowWidth = useWindowWidth();
  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;

  const { queryParams, updateQueryParams } = useQueryParams();

  const page = queryParams.page;

  const handlePage = (page: number) => {
    updateQueryParams({ page });
  };

  const [keyword, handleKeywordChange] = useInput(queryParams.keyword);

  const debouncedKeyword = useDebounce(keyword, 300);

  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(getSortingOptionByValue(queryParams.sort));

  const { selectedTagIds, selectedHotTopic, selectTopic } = useHotTopic();

  useEffect(() => {
    if (queryParams.sort === sortingOption.value) {
      return;
    }

    updateQueryParams({ sort: sortingOption.value, page: FIRST_PAGE });
  }, [queryParams.sort, sortingOption, updateQueryParams]);

  useEffect(() => {
    if (queryParams.keyword === debouncedKeyword) {
      return;
    }

    updateQueryParams({ keyword: debouncedKeyword, page: FIRST_PAGE });
  }, [queryParams.keyword, debouncedKeyword, updateQueryParams]);

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handlePage(FIRST_PAGE);
    }
  };

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '5rem' }}>
      <Flex direction='column' justify='flex-start' gap='1rem' width='100%'>
        {isMobile ? (
          <Heading.XSmall color='black'>
            {selectedHotTopic ? `🔥 [ ${selectedHotTopic} ] 보는 중` : '🔥 지금 인기있는 토픽'}
          </Heading.XSmall>
        ) : (
          <Heading.Medium color='black'>
            {selectedHotTopic ? `🔥 [ ${selectedHotTopic} ] 보는 중` : '🔥 지금 인기있는 토픽'}
          </Heading.Medium>
        )}
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
              handlePage={handlePage}
              sortingOption={sortingOption}
              keyword={debouncedKeyword}
              tagIds={selectedTagIds}
            />
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>
    </Flex>
  );
};

export default TemplateExplorePage;

const TemplateList = ({
  page,
  handlePage,
  sortingOption,
  keyword,
  tagIds,
}: {
  page: number;
  handlePage: (page: number) => void;
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
  const paginationSizes = templateData?.paginationSizes || 0;

  const windowWidth = useWindowWidth();

  const handlePageChange = (page: number) => {
    scroll.top();
    handlePage(page);
  };

  return (
    <>
      {templateList.length === 0 ? (
        isPending ? (
          <LoadingBall />
        ) : (
          <NoResults>검색 결과가 없습니다.</NoResults>
        )
      ) : (
        <S.TemplateListSectionWrapper>
          {isFetching && <TemplateListSectionLoading />}
          {!isLoading && (
            <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
              {templateList.map((template) => (
                <TemplateCard key={template.id} template={template} />
              ))}
            </S.TemplateExplorePageContainer>
          )}
        </S.TemplateListSectionWrapper>
      )}

      {templateList.length !== 0 && (
        <Flex justify='center' gap='0.5rem' margin='1rem 0' width='100%'>
          <PagingButtons currentPage={page} paginationSizes={paginationSizes} onPageChange={handlePageChange} />
        </Flex>
      )}
    </>
  );
};
