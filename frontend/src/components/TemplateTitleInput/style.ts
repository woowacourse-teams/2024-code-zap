import styled from '@emotion/styled';

export const TemplateTitleInput = styled.input`
  width: 100%;
  padding: 10px 0;
  background: none;
  border: none;
  border-bottom: 1px solid #555555;
  color: #cccccc;
  font-size: 16px;

  &::placeholder {
    color: #808080;
  }

  &:focus {
    outline: none;
    border-bottom: 1px solid #cccccc;
  }
`;

export const InputWrapper = styled.div`
  position: relative;
  margin: 20px 0;
  width: 100%;
`;
