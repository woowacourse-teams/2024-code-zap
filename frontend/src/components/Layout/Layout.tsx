import { ErrorBoundary } from '@sentry/react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { useEffect, useRef } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import { ApiError } from '@/api/Error/ApiError';
import { HTTP_STATUS } from '@/api/Error/statusCode';
import { Footer, Header, ScrollTopButton } from '@/components';
import { useHeaderHeight } from '@/hooks/useHeaderHeight';
import { ForbiddenPage, NotFoundPage } from '@/pages';

import * as S from './Layout.style';

const Layout = () => {
  const headerRef = useRef<HTMLDivElement>(null);
  const { setHeaderHeight } = useHeaderHeight();
  const location = useLocation();

  useEffect(() => {
    if (headerRef.current) {
      setHeaderHeight(headerRef.current.offsetHeight);
    }
  }, [setHeaderHeight]);

  return (
    <S.LayoutContainer>
      <Header headerRef={headerRef} />
      <S.Wrapper>
        <QueryErrorResetBoundary>
          {({ reset }) => (
            <ErrorBoundary
              fallback={(fallbackProps) => {
                const error = fallbackProps.error;

                if (error instanceof ApiError) {
                  if (error.statusCode === HTTP_STATUS.FORBIDDEN) {
                    return <ForbiddenPage resetError={fallbackProps.resetError} error={error} />;
                  }
                }

                return <NotFoundPage {...fallbackProps} />;
              }}
              onReset={reset}
              key={location.pathname}
            >
              <Outlet />
            </ErrorBoundary>
          )}
        </QueryErrorResetBoundary>
      </S.Wrapper>
      <ScrollTopButton />
      <Footer />
    </S.LayoutContainer>
  );
};

export default Layout;
