import styled from '@emotion/styled';
import type { Props } from './Text';

const weights = {
  regular: 400,
  bold: 700,
};

export const TextWrapper = styled.span<Props & { size: string }>`
  font-size: ${({ size }) => size};
  font-weight: ${({ weight = 'regular' }) => weights[weight]};
  color: ${({ color = 'white' }) => color};
`;
