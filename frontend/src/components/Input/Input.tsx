import { InputHTMLAttributes } from 'react';
import searchIcon from '@/assets/images/search.png';
import * as S from './style';

export interface Props extends InputHTMLAttributes<HTMLInputElement> {
  type?: 'text' | 'email' | 'password' | 'search';
  width?: string;
  height?: string;
  fontSize?: string;
  fontWeight?: string;
}

const Input = ({ type, width, height, fontSize, fontWeight, ...rests }: Props) => (
  <S.InputWrapper>
    {type === 'search' && <S.SearchIcon src={searchIcon} alt='search icon' />}
    <S.Input
      type={type}
      width={width}
      height={height}
      fontSize={fontSize}
      fontWeight={fontWeight}
      formNoValidate
      {...rests}
    />
  </S.InputWrapper>
);

export default Input;
