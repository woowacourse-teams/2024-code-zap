import styled from '@emotion/styled';
import type { Props } from './Flex';

export const FlexContainer = styled.div<Props>`
  display: flex;
  flex: ${(props) => props.flex};
  flex-flow: ${(props) => props.direction} ${(props) => props.wrap};
  gap: ${(props) => props.gap};
  align-items: ${(props) => props.align};
  justify-content: ${(props) => props.justify};

  width: ${(props) => props.width};
  height: ${(props) => props.height};
  margin: ${(props) => props.margin};
  padding: ${(props) => props.padding};
`;
