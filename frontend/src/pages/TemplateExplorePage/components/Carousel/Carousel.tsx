import { useState, useCallback, useRef } from 'react';

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

  const viewportRef = useRef<HTMLDivElement>(null);
  const [currentIndex, setCurrentIndex] = useState(0);

  const ITEM_WIDTH = isMobile ? 144 : 300; // 각 아이템의 넓이
  const ITEM_GAP = 12; //아이템 간의 간격
  const MOVE_DISTANCE = ITEM_WIDTH + ITEM_GAP;
  const VISIBLE_ITEMS = isMobile ? 2 : 3; // 실제 온전하게 보이는 아이템의 개수

  const moveCarousel = useCallback(
    (direction: 'prev' | 'next') => {
      if (!viewportRef.current) {
        return;
      }

      if (direction === 'prev' && currentIndex === 0) {
        return;
      }

      const maxIndex = items.length - VISIBLE_ITEMS;

      if (direction === 'next' && currentIndex >= maxIndex) {
        viewportRef.current.scrollLeft = viewportRef.current.scrollWidth;

        return;
      }

      const newIndex = direction === 'next' ? currentIndex + 1 : currentIndex - 1;
      const scrollPosition = newIndex * MOVE_DISTANCE;

      viewportRef.current.scrollTo({
        left: scrollPosition,
        behavior: 'smooth',
      });

      setCurrentIndex(newIndex);
    },
    [currentIndex, items.length, MOVE_DISTANCE, VISIBLE_ITEMS],
  );

  const handleScroll = useCallback(() => {
    if (!viewportRef.current) {
      return;
    }

    const newIndex = Math.round(viewportRef.current.scrollLeft / MOVE_DISTANCE);

    setCurrentIndex(newIndex);
  }, [MOVE_DISTANCE]);

  return (
    <S.CarouselContainer>
      {!isMobile && <S.PrevIcon onClick={() => moveCarousel('prev')} />}
      <S.CarouselViewport ref={viewportRef} onScroll={handleScroll}>
        <S.CarouselList>
          {items.map((item) => (
            <S.CarouselItem key={item.id}>{item.content}</S.CarouselItem>
          ))}
        </S.CarouselList>
      </S.CarouselViewport>
      {!isMobile && <S.NextIcon onClick={() => moveCarousel('next')} />}
    </S.CarouselContainer>
  );
};

export default Carousel;
