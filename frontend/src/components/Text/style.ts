import styled from '@emotion/styled';
import { Props } from './Text';

type StyleProps = Pick<Props, 'weight' | 'color'> & { size: string };

const weights = {
  regular: 400,
  bold: 700,
};

export const TextWrapper = styled.span<StyleProps>`
  font-size: ${({ size }) => `${size}rem`};
  font-weight: ${({ weight = 'regular' }) => weights[weight]};
  color: ${({ color = 'white' }) => color};
`;
