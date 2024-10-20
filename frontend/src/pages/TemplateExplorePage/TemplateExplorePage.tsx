import { useState } from 'react';
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
  TemplateCard,
} from '@/components';
import { useDebounce, useDropdown, useInput, useWindowWidth } from '@/hooks';
import { useTemplateExploreQuery } from '@/queries/templates';
import { theme } from '@/style/theme';
import { scroll } from '@/utils';

import { Carousel, CarouselItem } from './components';
import * as S from './TemplateExplorePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateExplorePage = () => {
  const [page, setPage] = useState<number>(1);
  const [keyword, handleKeywordChange] = useInput('');
  const debouncedKeyword = useDebounce(keyword, 300);

  const { currentValue: sortingOption, ...dropdownProps } = useDropdown(DEFAULT_SORTING_OPTION);
  const { data: templateData, isPending } = useTemplateExploreQuery({
    sort: sortingOption.key,
    page,
    keyword: debouncedKeyword,
  });
  const windowWidth = useWindowWidth();

  const templateList = templateData?.templates || [];
  const totalPages = templateData?.totalPages || 0;

  const handlePageChange = (page: number) => {
    scroll.top();
    setPage(page);
  };

  const handleSearchSubmit = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      setPage(1);
    }
  };

  const SampleItem = (bg: string, text: string) => <CarouselItem background={bg} title={text} />;

  return (
    <Flex direction='column' gap='4rem' align='flex-start' css={{ paddingTop: '2.5rem' }}>
      <Flex direction='column' width='100%' gap='2.5rem'>
        <Heading.Small color={theme.color.light.secondary_800}>{'🔥지금 인기있는 토픽'}</Heading.Small>
        <Carousel
          count={4}
          duration={0.4}
          interval={5}
          items={[
            SampleItem('lightCoral', '우테코프리코스'),
            SampleItem('lightBlue', '프론트엔드'),
            SampleItem('lightGreen', '백엔드'),
            SampleItem('lightYellow', '안드로이드'),
          ]}
        />
      </Flex>

      <Flex width='100%' direction='column' gap='1.5rem'>
        <Flex justify='flex-start' align='center' gap='1rem' margin='1rem 0'>
          <ZapzapLogo width={28} height={28} />
          <Heading.Small color='black'>{'여러 템플릿을 구경해보세요:)'}</Heading.Small>
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
