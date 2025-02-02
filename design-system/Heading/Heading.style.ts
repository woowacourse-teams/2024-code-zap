import styled from '@emotion/styled';

interface Props {
  color: string;
  weight: number;
  size: string;
  textDecoration?: 'none' | 'underline' | 'overline' | 'line-through';
}

export const HeadingElement = styled.span<Props>`
  font-size: ${({ size }) => size};
  font-weight: ${({ weight }) => weight};
  line-height: 100%;
  color: ${({ color }) => color};
  text-decoration: ${({ textDecoration }) => textDecoration || 'none'};
`;
