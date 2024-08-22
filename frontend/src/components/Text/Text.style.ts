import styled from '@emotion/styled';

interface Props {
  color: string;
  weight: number;
  size: string;
  textDecoration?: 'none' | 'underline' | 'overline' | 'line-through';
}

export const TextElement = styled.span<Props>`
  font-size: ${({ size }) => size};
  font-weight: ${({ weight }) => weight};
  line-height: ${({ size }) => (size === '2.5rem' || size === '3rem' ? '120%' : 'normal')};
  color: ${({ color }) => color};
  text-decoration: ${({ textDecoration }) => textDecoration || 'none'};
`;
