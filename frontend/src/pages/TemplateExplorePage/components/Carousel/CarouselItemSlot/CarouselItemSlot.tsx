import { ReactNode } from 'react';

import type { DIrection, State } from '../Carousel';
import * as S from './CarouselItemSlot.style';

interface Props {
  children: ReactNode;
  background?: string;
  duration?: number;
  moveDirection?: DIrection;
  state?: State;
}

const CarouselItemSlot = ({ children, duration = 0.3, moveDirection = 'static', state = 'common' }: Props) => (
  <S.CarouselItemSlotWrapper duration={duration} moveDirection={moveDirection} state={state}>
    {children}
  </S.CarouselItemSlotWrapper>
);

export default CarouselItemSlot;
