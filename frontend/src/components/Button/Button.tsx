import React from 'react';
import { buttonStyle, stylesBySize, stylesByType, textTypeStyle } from './style';

interface Props {
  children: React.ReactNode;
  onClick?: (e?: React.MouseEvent<HTMLButtonElement>) => void;
  type?: 'default' | 'outlined' | 'text';
  size?: 'small' | 'medium';
  width?: string | number;
  disabled?: boolean;
}

const Button = ({ children, onClick, type = 'default', size = 'medium', width, disabled = false }: Props) => (
  <button
    css={[buttonStyle, stylesByType[type], stylesBySize[size], type === 'text' ? textTypeStyle : width && { width }]}
    disabled={disabled}
    onClick={onClick}
  >
    {children}
  </button>
);

export default Button;
