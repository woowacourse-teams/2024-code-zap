import { ErrorBoundary } from '@sentry/react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { useEffect, useRef } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import { Header } from '@/components';
import { useHeaderHeight } from '@/hooks/utils/useHeaderHeight';
import { NotFoundPage } from '@/pages';
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
              fallback={(fallbackProps) => <NotFoundPage {...fallbackProps} />}
              onReset={reset}
              key={location.pathname}
            >
              <Outlet />
            </ErrorBoundary>
          )}
        </QueryErrorResetBoundary>
      </S.Wrapper>
    </S.LayoutContainer>
  );
};

export default Layout;
