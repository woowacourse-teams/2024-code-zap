import { PropsWithChildren } from 'react';

import { theme } from '@/style/theme';
import * as S from './Text.style';

type As = 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'p' | 'span';

const weights = { ...theme.font.weight };
const sizes = { ...theme.font.size.text };

interface Props {
  as?: As;
  color: string;
  weight?: keyof typeof weights;
}

const XLarge = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.xlarge}>
    {children}
  </S.TextElement>
);

const Large = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.large}>
    {children}
  </S.TextElement>
);

const Medium = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.medium}>
    {children}
  </S.TextElement>
);

const Small = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.small}>
    {children}
  </S.TextElement>
);

const XSmall = ({ children, as = 'span', color, weight = 'regular' }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.xsmall}>
    {children}
  </S.TextElement>
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
