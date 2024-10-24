// import { useState, useCallback } from 'react';

// import { useWindowWidth } from '@/hooks';
// import { BREAKING_POINT } from '@/style/styleConstants';

// import * as S from './Carousel.style';

// interface CarouselItem {
//   id: string;
//   content: React.ReactNode;
// }

// interface Props {
//   items: CarouselItem[];
// }

// const Carousel = ({ items }: Props) => {
//   const windowWidth = useWindowWidth();
//   const isMobile = windowWidth <= BREAKING_POINT.MOBILE;
//   const [currentIndex, setCurrentIndex] = useState(0);
//   const [isTransitioning, setIsTransitioning] = useState(false);

//   const ITEM_WIDTH = isMobile ? 144 : 300; // CarouselItem 넓이
//   const ITEM_GAP = 12; // 1rem
//   const MOVE_DISTANCE = ITEM_WIDTH + 4 + ITEM_GAP; // 4는 마진 추가

//   const translateX = -(currentIndex * MOVE_DISTANCE);
//   //MAX_TRANSLATE_X 는 현재 하드코딩으로 맞춰 놓음
//   const MAX_TRANSLATE_X = -((ITEM_WIDTH + 8) * (items.length - 4) + ITEM_GAP * items.length);

//   const moveCarousel = useCallback(
//     (direction: 'prev' | 'next') => {
//       if (direction === 'prev' && currentIndex === 0) {
//         return;
//       }

//       if (direction === 'next' && currentIndex === items.length - 3) {
//         return;
//       }

//       const newIndex = direction === 'next' ? currentIndex + 1 : currentIndex - 1;

//       setIsTransitioning(true);
//       setCurrentIndex(newIndex);
//     },
//     [currentIndex, items.length],
//   );

//   return (
//     <S.CarouselContainer>
//       {!isMobile && <S.PrevIcon onClick={() => moveCarousel('prev')} />}
//       <S.CarouselViewport>
//         <S.CarouselList translateX={Math.max(translateX, MAX_TRANSLATE_X)} transitioning={isTransitioning}>
//           {items.map((item) => (
//             <S.CarouselItem key={item.id}>{item.content}</S.CarouselItem>
//           ))}
//         </S.CarouselList>
//       </S.CarouselViewport>
//       {!isMobile && <S.NextIcon onClick={() => moveCarousel('next')} />}
//     </S.CarouselContainer>
//   );
// };

// export default Carousel;
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

  const ITEM_WIDTH = isMobile ? 144 : 300;
  const ITEM_GAP = 12;
  const MOVE_DISTANCE = ITEM_WIDTH + ITEM_GAP;
  const VISIBLE_ITEMS = isMobile ? 2 : 3;

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
