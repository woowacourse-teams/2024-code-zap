import styled from '@emotion/styled';
import type { Props } from './Flex';

export const FlexContainer = styled.div<Props>`
  display: flex;
  gap: ${(props) => props.gap};
  align-items: ${(props) => props.align};
  justify-content: ${(props) => props.justify};
  flex: ${(props) => props.flex};
  margin: ${(props) => props.margin};
  padding: ${(props) => props.padding};
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  flex-flow: ${(props) => props.direction} ${(props) => props.wrap};
`;
