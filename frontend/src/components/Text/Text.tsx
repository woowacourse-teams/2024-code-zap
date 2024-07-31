import { PropsWithChildren } from 'react';

import { theme } from '@/style/theme';
import * as S from './Test.style';

type As = 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'p' | 'span';

const weights = { ...theme.font.weight };
const sizes = { ...theme.font.size.text };

interface Props {
  as?: As;
  color: string;
  weight?: keyof typeof weights;
}

const XLarge = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextWrapper as={as} color={color} weight={weights[weight]} size={sizes.xlarge}>
    {children}
  </S.TextWrapper>
);

const Large = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextWrapper as={as} color={color} weight={weights[weight]} size={sizes.large}>
    {children}
  </S.TextWrapper>
);

const Medium = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextWrapper as={as} color={color} weight={weights[weight]} size={sizes.medium}>
    {children}
  </S.TextWrapper>
);

const Small = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextWrapper as={as} color={color} weight={weights[weight]} size={sizes.small}>
    {children}
  </S.TextWrapper>
);

const XSmall = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextWrapper as={as} color={color} weight={weights[weight]} size={sizes.xsmall}>
    {children}
  </S.TextWrapper>
);

const Text = Object.assign(
  {},
  {
    XLarge,
    Large,
    Medium,
    Small,
    XSmall,
  },
);

export default Text;
