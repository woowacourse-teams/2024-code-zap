import { useState, useCallback, useEffect, useRef } from 'react';

import { useWindowWidth } from '@/hooks';
import { BREAKING_POINT } from '@/style/styleConstants';

import * as S from './Carousel.style';

interface CarouselItem {
  id: string;
  content: React.ReactNode;
}

interface Props {
  items: CarouselItem[];
}

const Carousel = ({ items }: Props) => {
  const windowWidth = useWindowWidth();
  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isTransitioning, setIsTransitioning] = useState(false);
  const transitionTimer = useRef<NodeJS.Timeout | null>(null);

  const displayItems = [...items, ...items, ...items];

  const ITEM_WIDTH = isMobile ? 144 : 288; // CarouselItem 넓이
  const ITEM_GAP = 16; // 1rem
  const MOVE_DISTANCE = ITEM_WIDTH + ITEM_GAP;
  const originalLength = items.length;

  const translateX = -(currentIndex * MOVE_DISTANCE + originalLength * MOVE_DISTANCE);

  const moveCarousel = useCallback(
    (direction: 'prev' | 'next') => {
      const newIndex = direction === 'next' ? currentIndex + 1 : currentIndex - 1;

      setIsTransitioning(true);
      setCurrentIndex(newIndex);
    },
    [currentIndex],
  );

  const needsReposition = useCallback(
    (index: number) => {
      const threshold = originalLength;

      return index <= -threshold || index >= threshold;
    },
    [originalLength],
  );

  const resetPosition = useCallback(() => {
    setIsTransitioning(false);

    setCurrentIndex(currentIndex % originalLength);

    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        setIsTransitioning(true);
      });
    });
  }, [currentIndex, originalLength]);

  useEffect(() => {
    if (needsReposition(currentIndex)) {
      if (transitionTimer.current) {
        clearTimeout(transitionTimer.current);
      }

      transitionTimer.current = setTimeout(() => {
        resetPosition();
        setIsTransitioning(false);
      }, 300); // transition: transform 0.3s 이기 때문
    }

    return () => {
      if (transitionTimer.current) {
        clearTimeout(transitionTimer.current);
      }
    };
  }, [currentIndex, needsReposition, resetPosition]);

  return (
    <S.CarouselContainer>
      {!isMobile && <S.PrevIcon onClick={() => moveCarousel('prev')} />}
      <S.CarouselViewport>
        <S.CarouselList translateX={translateX} transitioning={isTransitioning}>
          {displayItems.map((item, idx) => (
            <S.CarouselItem key={item.id + idx}>{item.content}</S.CarouselItem>
          ))}
        </S.CarouselList>
      </S.CarouselViewport>
      {!isMobile && <S.NextIcon onClick={() => moveCarousel('next')} />}
    </S.CarouselContainer>
  );
};

export default Carousel;
