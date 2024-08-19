import { HTMLAttributes, PropsWithChildren } from 'react';

import * as S from './Guide.style';

interface Props extends HTMLAttributes<HTMLDivElement> {
  isOpen: boolean;
}

const Guide = ({ isOpen, children, ...rest }: PropsWithChildren<Props>) => (
  <S.Guide isOpen={isOpen} {...rest}>
    {children}
  </S.Guide>
);

export default Guide;
