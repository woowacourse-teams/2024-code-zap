import React from 'react';
import { style, searchStyle, inputWrapperStyle, iconStyle } from './style';
import searchIcon from '../../assets/images/search.png';

interface Props {
  value: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  type?: 'text' | 'email' | 'password' | 'search';
  disabled?: boolean;
}

const Input = ({ value, onChange, placeholder = '', type = 'text', disabled = false }: Props) => {
  return (
    <div css={inputWrapperStyle}>
      {type === 'search' && <img src={searchIcon} css={iconStyle} alt='search icon' />}
      <input
        css={[style, type === 'search' && searchStyle]}
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
