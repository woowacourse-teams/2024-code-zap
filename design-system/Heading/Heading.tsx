import { PropsWithChildren } from 'react';

import { theme } from '@design/style/theme';

import * as S from './Heading.style';

type As = 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'p' | 'span';

const weights = { ...theme.font.weight };
const sizes = { ...theme.font.size.heading };

type TextDecoration = 'none' | 'underline' | 'overline' | 'line-through';

interface Props {
  as?: As;
  color: string;
  weight?: keyof typeof weights;
  textDecoration?: TextDecoration;
}

const XLarge = ({
  children,
  as = 'span',
  color,
  weight = 'bold',
  textDecoration,
}: PropsWithChildren<Props>) => (
  <S.HeadingElement
    as={as}
    color={color}
    weight={weights[weight]}
    size={sizes.xlarge}
    textDecoration={textDecoration}
  >
    {children}
  </S.HeadingElement>
);

const Large = ({
  children,
  as = 'span',
  color,
  weight = 'bold',
  textDecoration,
}: PropsWithChildren<Props>) => (
  <S.HeadingElement
    as={as}
    color={color}
    weight={weights[weight]}
    size={sizes.large}
    textDecoration={textDecoration}
  >
    {children}
  </S.HeadingElement>
);

const Medium = ({
  children,
  as = 'span',
  color,
  weight = 'bold',
  textDecoration,
}: PropsWithChildren<Props>) => (
  <S.HeadingElement
    as={as}
    color={color}
    weight={weights[weight]}
    size={sizes.medium}
    textDecoration={textDecoration}
  >
    {children}
  </S.HeadingElement>
);

const Small = ({
  children,
  as = 'span',
  color,
  weight = 'bold',
  textDecoration,
}: PropsWithChildren<Props>) => (
  <S.HeadingElement
    as={as}
    color={color}
    weight={weights[weight]}
    size={sizes.small}
    textDecoration={textDecoration}
  >
    {children}
  </S.HeadingElement>
);

const XSmall = ({
  children,
  as = 'span',
  color,
  weight = 'bold',
  textDecoration,
}: PropsWithChildren<Props>) => (
  <S.HeadingElement
    as={as}
    color={color}
    weight={weights[weight]}
    size={sizes.xsmall}
    textDecoration={textDecoration}
  >
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
  }
);

export default Heading;
