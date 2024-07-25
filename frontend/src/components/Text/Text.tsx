import { PropsWithChildren } from 'react';
import { styleText } from './style';

export type FontWeight = 'regular' | 'bold';

export interface TextProps {
  weight?: FontWeight;
  color?: string;
}

const Text = ({ children }: PropsWithChildren<TextProps>) => {
  return <span>{children}</span>;
};

export default Text;

Text.Heading = function Heading({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <div css={styleText('4.2', weight, color)}>{children}</div>;
};

Text.Title = function Title({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <h1 css={styleText('3.2', weight, color)}>{children}</h1>;
};

Text.SubTitle = function SubTitle({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <h2 css={styleText('2.4', weight, color)}>{children}</h2>;
};

Text.Label = function Label({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <span css={styleText('1.8', weight, color)}>{children}</span>;
};

Text.Body = function Body({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <span css={styleText('1.6', weight, color)}>{children}</span>;
};

Text.Caption = function Caption({
  children,
  weight,
  color,
}: PropsWithChildren<TextProps>) {
  return <span css={styleText('1.4', weight, color)}>{children}</span>;
};
