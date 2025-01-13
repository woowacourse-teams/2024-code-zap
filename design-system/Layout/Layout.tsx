import { ErrorBoundary } from '@sentry/react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { Suspense, useEffect, useRef } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import { ApiError, HTTP_STATUS } from '@/api/Error';
import { Footer, Header, LoadingBall, ScrollTopButton } from '@/components';
import { useHeaderHeight } from '@/hooks';
import { ForbiddenPage, NotFoundPage } from '@/pages';

import * as S from './Layout.style';

const Layout = () => {
  const headerRef = useRef<HTMLDivElement>(null);
  const { setHeaderHeight } = useHeaderHeight();

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
            <GlobalErrorBoundary reset={reset}>
              <GlobalSuspense>
                <Outlet />
              </GlobalSuspense>
            </GlobalErrorBoundary>
          )}
        </QueryErrorResetBoundary>
      </S.Wrapper>
      <ScrollTopButton />
      <Footer />
    </S.LayoutContainer>
  );
};

export default Layout;

const GlobalSuspense = ({ children }: { children: JSX.Element }) => (
  <Suspense
    fallback={
      <div style={{ height: '100vh' }}>
        <LoadingBall />
      </div>
    }
  >
    {children}
  </Suspense>
);

const GlobalErrorBoundary = ({ children, reset }: { children: JSX.Element; reset: () => void }) => {
  const location = useLocation();

  return (
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
      {children}
    </ErrorBoundary>
  );
};
