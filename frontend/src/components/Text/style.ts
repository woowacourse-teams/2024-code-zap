import styled from '@emotion/styled';
import type { Props, TextWeight } from './Text';

const weights: Record<TextWeight, number> = {
  regular: 400,
  bold: 700,
};

export const TextWrapper = styled.span<Props & { weight: TextWeight; size: string }>`
  font-size: ${({ size }) => size};
  font-weight: ${({ weight }) => weights[weight]};
  color: ${({ color }) => color};
`;
