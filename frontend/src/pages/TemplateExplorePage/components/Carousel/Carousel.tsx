import { ReactNode, useEffect, useMemo, useState, useRef } from 'react';

import CarouselItemSlot from './CarouselItemSlot/CarouselItemSlot';
import { ChevronUpCircleIcon } from '../../../../assets/images/index';
import * as S from './Carousel.style';

export type DIrection = 'static' | 'left' | 'right';
export type State = 'leftOutside' | 'leftInside' | 'common' | 'rightInside' | 'rightOutside';

interface Props {
  count: number;
  duration?: number;
  interval?: number;
  items: ReactNode[];
}

const Carousel = ({ count, items, duration = 0, interval }: Props) => {
  const itemCount = items.length;
  const [firstItemIndex, setFirstItemIndex] = useState(0);
  const [moveDirection, setMoveDirection] = useState<DIrection>('static');
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const intervalIdRef = useRef<NodeJS.Timeout | null>(null);

  const clearAutoScrollTimer = () => {
    if (intervalIdRef.current) {
      clearInterval(intervalIdRef.current);
    }
  };

  const startAutoScrollTimer = () => {
    if (interval) {
      intervalIdRef.current = setInterval(() => {
        handleMoveButtonClick('left');
      }, interval * 1000);
    }
  };

  // 타이머를 설정하는 useEffect
  useEffect(() => {
    if (!interval) {
      return () => {};
    }

    startAutoScrollTimer();

    return () => {
      clearAutoScrollTimer();
    };
  }, [interval, itemCount]);

  const generateItems = useMemo(() => {
    const orderedItems = [] as ReactNode[];

    for (let i = 0; i < count + 2; i++) {
      const state: State =
        i === 0
          ? 'leftOutside'
          : i === 1
            ? 'leftInside'
            : i === count
              ? 'rightInside'
              : i === count + 1
                ? 'rightOutside'
                : 'common';

      orderedItems.push(
        <CarouselItemSlot key={Math.random()} duration={duration} moveDirection={moveDirection} state={state}>
          {items[(itemCount + firstItemIndex + i - 1) % itemCount]}
        </CarouselItemSlot>,
      );
    }

    return orderedItems;
  }, [count, moveDirection, duration, firstItemIndex, itemCount, items]);

  const handleMoveButtonClick = (direction: 'left' | 'right') => {
    if (isButtonDisabled) {
      return;
    }

    setFirstItemIndex((prev) => (prev - (direction === 'left' ? -1 : 1)) % itemCount);
    setMoveDirection(direction);

    setIsButtonDisabled(true);
    setTimeout(() => {
      setIsButtonDisabled(false);
    }, 150);
  };

  return (
    <S.CarouselContainer onMouseEnter={clearAutoScrollTimer} onMouseLeave={startAutoScrollTimer}>
      <S.MoveButton position={'left'} onClick={() => handleMoveButtonClick('right')} disabled={isButtonDisabled}>
        <ChevronUpCircleIcon width={40} height={40} />
      </S.MoveButton>
      <S.CarouselItemSlotsContainer>{generateItems}</S.CarouselItemSlotsContainer>
      <S.MoveButton position={'right'} onClick={() => handleMoveButtonClick('left')} disabled={isButtonDisabled}>
        <ChevronUpCircleIcon width={40} height={40} />
      </S.MoveButton>
    </S.CarouselContainer>
  );
};

export default Carousel;
