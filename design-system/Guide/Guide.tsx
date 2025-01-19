import { HTMLAttributes, PropsWithChildren } from 'react';

import * as S from './Guide.style';

interface Props extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
}

const Guide = ({ isOpen, children, ...rest }: PropsWithChildren<Props>) => (
  <S.GuideContainer isOpen={isOpen} {...rest}>
    {children}
  </S.GuideContainer>
);

export default Guide;
