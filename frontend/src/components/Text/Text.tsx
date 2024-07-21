import { ReactNode } from 'react';
import * as S from './style';

export interface Props {
  children?: ReactNode;
  weight?: 'regular' | 'bold';
  color?: string;
}

const Heading = ({ children, weight, color }: Props) => (
  <S.TextWrapper size='4.2' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Title = ({ children, weight, color }: Props) => (
  <S.TextWrapper as='h1' size='3.2' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const SubTitle = ({ children, weight, color }: Props) => (
  <S.TextWrapper as='h2' size='2.4' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Label = ({ children, weight, color }: Props) => (
  <S.TextWrapper size='1.8' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Body = ({ children, weight, color }: Props) => (
  <S.TextWrapper size='1.6' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Caption = ({ children, weight, color }: Props) => (
  <S.TextWrapper size='1.4' weight={weight} color={color}>
    {children}
  </S.TextWrapper>
);

const Text = Object.assign({
  Heading,
  Title,
  SubTitle,
  Label,
  Body,
  Caption,
});

export default Text;
