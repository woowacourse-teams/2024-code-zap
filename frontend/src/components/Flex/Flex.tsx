import { FlexContainer } from './style';
import { ReactNode } from 'react';

export interface FlexProps {
  children: ReactNode;
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

const Flex: React.FC<FlexProps> = ({
  children,
  direction = 'row',
  justify = 'flex-start',
  align = 'stretch',
  wrap = 'nowrap',
  gap = '0',
  width = 'auto',
  height = 'auto',
  padding = '0',
  margin = '0',
  flex = 'none',
  ...props
}) => {
  return (
    <FlexContainer
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
    </FlexContainer>
  );
};

export default Flex;
