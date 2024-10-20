import { Heading } from '@/components';

import * as S from './CarouselItem.style';

interface Props {
  title?: string;
  color?: string;
  background?: string;
  onClick?: () => void;
}

const CarouselItem = ({ title = '', color = 'black', background = 'white', onClick }: Props) => (
  <S.CarouselItemWrapper background={background} onClick={onClick}>
    <Heading.XSmall color={color}>{title}</Heading.XSmall>
  </S.CarouselItemWrapper>
);

export default CarouselItem;
