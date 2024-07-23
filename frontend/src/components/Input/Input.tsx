import searchIcon from '../../assets/images/search.png';
import * as S from './style';

export interface Props {
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

const Input = ({ value, onChange, placeholder, type, disabled, width, height, fontSize, fontWeight }: Props) => (
  <S.InputWrapper>
    {type === 'search' && <S.SearchIcon src={searchIcon} alt='search icon' />}
    <S.Input
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      type={type}
      disabled={disabled}
      width={width}
      height={height}
      fontSize={fontSize}
      fontWeight={fontWeight}
      formNoValidate={type === 'email'}
    />
  </S.InputWrapper>
);

export default Input;
