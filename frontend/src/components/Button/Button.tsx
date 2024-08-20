import { ButtonHTMLAttributes } from 'react';

import * as S from './Button.style';

export type ButtonVariant = 'contained' | 'outlined' | 'text';
type ButtonSize = 'small' | 'medium';
type ButtonWeight = 'regular' | 'bold';
type ButtonHoverStyle = 'base' | 'none';

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  size?: ButtonSize;
  weight?: ButtonWeight;
  hoverStyle?: ButtonHoverStyle;
  fullWidth?: boolean;
}

const Button = ({
  children,
  variant = 'contained',
  size = 'medium',
  weight = 'bold',
  hoverStyle = 'base',
  fullWidth = false,
  ...rest
}: Props) => (
  <S.Button variant={variant} size={size} weight={weight} hoverStyle={hoverStyle} fullWidth={fullWidth} {...rest}>
    {children}
  </S.Button>
);

export default Button;
