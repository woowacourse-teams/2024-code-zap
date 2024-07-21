import styled from '@emotion/styled';
import { Props } from './Flex';

type StyleProps = Pick<
  Props,
  'flex' | 'width' | 'height' | 'margin' | 'padding' | 'align' | 'direction' | 'wrap' | 'gap' | 'justify'
>;

export const FlexContainer = styled.div<StyleProps>`
  display: flex;
  flex: ${(props) => props.flex};
  flex-flow: ${(props) => props.direction};
  flex-wrap: ${(props) => props.wrap};
  gap: ${(props) => props.gap};
  align-items: ${(props) => props.align};
  justify-content: ${(props) => props.justify};

  width: ${(props) => props.width};
  height: ${(props) => props.height};
  margin: ${(props) => props.margin};
  padding: ${(props) => props.padding};
`;
