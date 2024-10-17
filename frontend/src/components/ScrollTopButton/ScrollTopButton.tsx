import { ArrowUpIcon } from '@/assets/images';
import { scroll } from '@/utils';

import * as S from './ScrollTopButton.style';

const ScrollTopButton = () => (
  <S.ScrollTopButton
    onClick={() => {
      scroll.top('smooth');
    }}
  >
    <ArrowUpIcon aria-label='맨 위로' />
  </S.ScrollTopButton>
);

export default ScrollTopButton;
