import * as S from './style';

interface Props {
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const TemplateTitleInput = ({ placeholder, value, onChange }: Props) => {
  return (
    <S.InputWrapper>
      <S.TemplateTitleInput placeholder={placeholder} value={value} onChange={onChange} />
    </S.InputWrapper>
  );
};

export default TemplateTitleInput;
