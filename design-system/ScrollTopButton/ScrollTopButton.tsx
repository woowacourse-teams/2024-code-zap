import { useEffect, useState, useRef } from 'react';
import { useLocation } from 'react-router-dom';

import { ArrowUpIcon } from '@/assets/images';
import { useWindowWidth } from '@/hooks';
import { END_POINTS } from '@/routes';
import { BREAKING_POINT } from '@design/style/styleConstants';
import { scroll } from '@/utils';

import * as S from './ScrollTopButton.style';

const ScrollTopButton = () => {
  const location = useLocation();
  const [isVisible, setIsVisible] = useState(false);
  const sentinelRef = useRef<HTMLDivElement>(null);

  const windowWidth = useWindowWidth();

  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;
  const isTemplateUpload =
    isMobile && location.pathname === END_POINTS.TEMPLATES_UPLOAD;

  useEffect(() => {
    const sentinel = sentinelRef.current;

    if (!sentinel) {
      return;
    }

    const observerOptions: IntersectionObserverInit = {
      root: null,
      rootMargin: '0px',
      threshold: [0, 1.0],
    };

    const observerCallback: IntersectionObserverCallback = (entries) => {
      // entries[0].isIntersecting이 false일 때 (sentinel이 화면에서 벗어났을 때) 버튼 보여줌
      const entry = entries[0];
      const shouldShow = !entry.isIntersecting || entry.intersectionRatio < 1;

      void setIsVisible(shouldShow);
    };

    const observer = new IntersectionObserver(
      observerCallback,
      observerOptions
    );

    observer.observe(sentinel);

    // eslint-disable-next-line consistent-return
    return () => {
      observer.disconnect();
    };
  }, []);

  return (
    <>
      <S.Sentinel ref={sentinelRef} aria-hidden='true' />
      <S.ScrollTopButtonContainer
        isVisible={isVisible}
        isTemplateUpload={isTemplateUpload}
        onClick={() => {
          scroll.top('smooth');
        }}
      >
        <ArrowUpIcon aria-label='맨 위로' />
      </S.ScrollTopButtonContainer>
    </>
  );
};

export default ScrollTopButton;
