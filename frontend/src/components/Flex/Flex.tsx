import { PropsWithChildren } from 'react';
import * as S from './style';

export interface Props extends React.HTMLAttributes<HTMLDivElement> {
  direction?: 'row' | 'row-reverse' | 'column' | 'column-reverse';
  justify?: 'flex-start' | 'flex-end' | 'center' | 'space-between' | 'space-around' | 'space-evenly';
  align?: 'stretch' | 'flex-start' | 'flex-end' | 'center' | 'baseline';
  wrap?: 'nowrap' | 'wrap' | 'wrap-reverse';
  gap?: string;
  width?: string;
  height?: string;
  padding?: string;
  margin?: string;
  flex?: string;
}

const Flex = ({
  children,
  direction,
  justify,
  align,
  wrap,
  gap,
  width,
  height,
  padding,
  margin,
  flex,
  ...props
}: PropsWithChildren<Props>) => (
  <S.FlexContainer
    direction={direction}
    justify={justify}
    align={align}
    wrap={wrap}
    gap={gap}
    width={width}
    height={height}
    padding={padding}
    margin={margin}
    flex={flex}
    {...props}
  >
    {children}
  </S.FlexContainer>
);

export default Flex;
