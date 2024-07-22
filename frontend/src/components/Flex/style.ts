import styled from '@emotion/styled';
import type { Props } from './Flex';

interface StyleProps
  extends Pick<
    Props,
    'flex' | 'width' | 'height' | 'margin' | 'padding' | 'align' | 'direction' | 'wrap' | 'gap' | 'justify'
  > {}

export const FlexContainer = styled.div<StyleProps>`
  display: flex;
  flex: ${({ flex }) => flex};
  flex-direction: ${({ direction }) => direction};
  flex-wrap: ${({ wrap }) => wrap};
  gap: ${({ gap }) => gap};
  align-items: ${({ align }) => align};
  justify-content: ${({ justify }) => justify};

  width: ${({ width }) => width};
  height: ${({ height }) => height};
  margin: ${({ margin }) => margin};
  padding: ${({ padding }) => padding};
`;
