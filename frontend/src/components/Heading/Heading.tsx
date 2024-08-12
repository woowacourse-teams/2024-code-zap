import { PropsWithChildren } from 'react';

import { theme } from '@/style/theme';
import * as S from './Heading.style';

type As = 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'p' | 'span';

const weights = { ...theme.font.weight };
const sizes = { ...theme.font.size.heading };

interface Props {
  as?: As;
  color: string;
  weight?: keyof typeof weights;
}

const XLarge = ({ children, as = 'span', color, weight = 'bold' }: PropsWithChildren<Props>) => (
  <S.HeadingElement as={as} color={color} weight={weights[weight]} size={sizes.xlarge}>
    {children}
  </S.HeadingElement>
);

const Large = ({ children, as = 'span', color, weight = 'bold' }: PropsWithChildren<Props>) => (
  <S.HeadingElement as={as} color={color} weight={weights[weight]} size={sizes.large}>
    {children}
  </S.HeadingElement>
);

const Medium = ({ children, as = 'span', color, weight = 'bold' }: PropsWithChildren<Props>) => (
  <S.HeadingElement as={as} color={color} weight={weights[weight]} size={sizes.medium}>
    {children}
  </S.HeadingElement>
);

const Small = ({ children, as = 'span', color, weight = 'bold' }: PropsWithChildren<Props>) => (
  <S.HeadingElement as={as} color={color} weight={weights[weight]} size={sizes.small}>
    {children}
  </S.HeadingElement>
);

const XSmall = ({ children, as = 'span', color, weight = 'bold' }: PropsWithChildren<Props>) => (
  <S.HeadingElement as={as} color={color} weight={weights[weight]} size={sizes.xsmall}>
    {children}
  </S.HeadingElement>
);

const Heading = Object.assign(
  {},
  {
    XLarge,
    Large,
    Medium,
    Small,
    XSmall,
  },
);

export default Heading;
