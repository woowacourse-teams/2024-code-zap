import { ButtonHTMLAttributes } from 'react';
import * as S from './style';

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant: 'contained' | 'outlined' | 'text';
  size: 'small' | 'medium' | 'filled';
}

const Button = ({ children, onClick, variant, size, ...rest }: Props) => (
  <S.Button variant={variant} size={size} onClick={onClick} {...rest}>
    {children}
  </S.Button>
);

export default Button;
