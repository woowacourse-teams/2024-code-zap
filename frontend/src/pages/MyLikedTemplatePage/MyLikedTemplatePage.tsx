import { useTranslation } from 'react-i18next';
import { Link, useParams } from 'react-router-dom';

import {
  Flex,
  Heading,
  LoadingBall,
  NoResults,
  PagingButtons,
  TemplateCard,
} from '@/components';
import { useQueryParams, useWindowWidth } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { ForbiddenPage } from '@/pages';
import { TemplateListSectionLoading } from '@/pages/MemberTemplatePage/components';
import { useLikedTemplateListQuery } from '@/queries/templates/useLikedTemplateListQuery';
import { ROUTE_END_POINT } from '@/routes/endPoints';
import { useTrackPageViewed } from '@/service/amplitude';
import { BREAKING_POINT } from '@/style/styleConstants';

import * as S from './MyLikedTemplatePage.style';

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const MyLikedTemplatePage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 내가 좋아하는 템플릿 페이지' });
  const windowWidth = useWindowWidth();
  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;

  const { memberId } = useParams<{ memberId: string }>();
  const routeMemberId = Number(memberId);
  const {
    memberInfo: { memberId: currentMemberId },
  } = useAuth();

  const isMine = routeMemberId === currentMemberId;

  const { queryParams, updateQueryParams } = useQueryParams();
  const page = queryParams.page;

  const handlePageChange = (page: number) => {
    updateQueryParams({ page });
  };

  const {
    data: templateData,
    isLoading,
    isPending,
    isFetching,
  } = useLikedTemplateListQuery({ page });
  const templateList = templateData?.templates || [];
  const paginationSizes = templateData?.paginationSizes || 0;

  const { t } = useTranslation('MyLikedTemplatePage');

  if (!isMine) {
    return <ForbiddenPage />;
  }

  return (
    <>
      <S.PageTitle>
        {isMobile ? (
          <Heading.XSmall color='black'>{t('title')}</Heading.XSmall>
        ) : (
          <Heading.Medium color='black'>{t('title')}</Heading.Medium>
        )}
      </S.PageTitle>
      {templateList.length === 0 ? (
        isPending ? (
          <LoadingBall />
        ) : (
          <NoResults>{t('noResults')}</NoResults>
        )
      ) : (
        <S.TemplateListSectionWrapper>
          {isFetching && <TemplateListSectionLoading />}
          {!isLoading && (
            <S.TemplateExplorePageContainer cols={getGridCols(windowWidth)}>
              {templateList.map((template) => (
                <Link
                  to={ROUTE_END_POINT.template(template.id)}
                  key={template.id}
                >
                  <TemplateCard template={template} />
                </Link>
              ))}
            </S.TemplateExplorePageContainer>
          )}
        </S.TemplateListSectionWrapper>
      )}

      {templateList.length !== 0 && (
        <Flex justify='center' gap='0.5rem' margin='1rem 0' width='100%'>
          <PagingButtons
            currentPage={page}
            paginationSizes={paginationSizes}
            onPageChange={handlePageChange}
          />
        </Flex>
      )}
    </>
  );
};

export default MyLikedTemplatePage;
