import styled from '@emotion/styled';

import type { Props, TextWeight } from './Text';

export const TextWrapper = styled.span<Props & { weight: TextWeight; size: string }>`
  font-size: ${({ size }) => size};
  font-weight: ${({ theme, weight }) => theme.font.weight[weight]};
  color: ${({ color }) => color};
`;
