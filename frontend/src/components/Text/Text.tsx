import { PropsWithChildren } from 'react';

import { theme } from '@/style/theme';

import * as S from './Text.style';

type As = 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'p' | 'span';

const weights = { ...theme.font.weight };
const sizes = { ...theme.font.size.text };

type TextDecoration = 'none' | 'underline' | 'overline' | 'line-through';

interface Props {
  as?: As;
  color: string;
  weight?: keyof typeof weights;
  textDecoration?: TextDecoration;
}

const XLarge = ({ children, as = 'span', color, weight = 'regular', textDecoration }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.xlarge} textDecoration={textDecoration}>
    {children}
  </S.TextElement>
);

const Large = ({ children, as = 'span', color, weight = 'regular', textDecoration }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.large} textDecoration={textDecoration}>
    {children}
  </S.TextElement>
);

const Medium = ({ children, as = 'span', color, weight = 'regular', textDecoration }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.medium} textDecoration={textDecoration}>
    {children}
  </S.TextElement>
);

const Small = ({ children, as = 'span', color, weight = 'regular', textDecoration }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.small} textDecoration={textDecoration}>
    {children}
  </S.TextElement>
);

const XSmall = ({ children, as = 'span', color, weight = 'regular', textDecoration }: PropsWithChildren<Props>) => (
  <S.TextElement as={as} color={color} weight={weights[weight]} size={sizes.xsmall} textDecoration={textDecoration}>
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
