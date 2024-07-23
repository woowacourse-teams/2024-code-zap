import { PropsWithChildren } from 'react';
import * as S from './style';

export type TextWeight = 'regular' | 'bold';

export interface Props {
  weight?: TextWeight;
  color?: string;
}

const Heading = ({ children, weight = 'bold', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper size='4.2' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Title = ({ children, weight = 'bold', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper as='h1' size='3.2' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const SubTitle = ({ children, weight = 'bold', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper as='h2' size='2.4' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Label = ({ children, weight = 'bold', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper size='1.8' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Body = ({ children, weight = 'regular', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper size='1.6' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Caption = ({ children, weight = 'regular', color }: PropsWithChildren<Props>) => (
  <S.TextWrapper size='1.4' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Text = {
  Heading,
  Title,
  SubTitle,
  Label,
  Body,
  Caption,
};

export default Text;
