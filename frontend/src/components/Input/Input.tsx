import React from 'react';
import { inputStyle, searchStyle, inputWrapperStyle, iconStyle } from './style';
import searchIcon from '../../assets/images/search.png';

interface Props {
  value: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  type?: 'text' | 'email' | 'password' | 'search';
  disabled?: boolean;
  width?: string;
  height?: string;
  fontSize?: string;
  fontWeight?: string;
}

const Input = ({
  value,
  onChange,
  placeholder = '',
  type = 'text',
  disabled = false,
  width,
  height,
  fontSize,
  fontWeight,
}: Props) => {
  return (
    <div css={inputWrapperStyle(width)}>
      {type === 'search' && <img src={searchIcon} css={iconStyle} alt='search icon' />}
      <input
        css={[inputStyle({ width, height, fontSize, fontWeight }), type === 'search' && searchStyle]}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        type={type}
        disabled={disabled}
        {...(type === 'email' && { formNoValidate: true })}
      />
    </div>
  );
};

export default Input;
