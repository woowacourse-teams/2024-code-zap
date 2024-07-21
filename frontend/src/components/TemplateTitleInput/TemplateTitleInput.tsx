import * as S from './style';

interface Props {
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const TemplateTitleInput = ({ placeholder, value, onChange }: Props) => (
  <S.TemplateTitleInputWrapper>
    <S.TemplateTitleInput placeholder={placeholder} value={value} onChange={onChange} />
  </S.TemplateTitleInputWrapper>
);

export default TemplateTitleInput;
