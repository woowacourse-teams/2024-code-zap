import styled from '@emotion/styled';

interface Props {
  color: string;
  weight: number;
  size: string;
}

export const HeadingElement = styled.span<Props>`
  font-size: ${({ size }) => size};
  font-weight: ${({ weight }) => weight};
  color: ${({ color }) => color};
`;
