import * as S from './style';

const TemplateTitleInput = ({ placeholder }: { placeholder: string }) => {
  return (
    <S.InputWrapper>
      <S.TemplateTitleInput placeholder={placeholder} />
    </S.InputWrapper>
  );
};

export default TemplateTitleInput;
