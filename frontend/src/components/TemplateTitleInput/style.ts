import styled from '@emotion/styled';

export const TemplateTitleInput = styled.input`
  padding: 10px 0;
  border: none;
  border-bottom: 1px solid #555;
  width: 100%;
  background: none;
  font-size: 16px;
  color: #ccc;

  &::placeholder {
    color: #808080;
  }

  &:focus {
    border-bottom: 1px solid #ccc;
    outline: none;
  }
`;

export const InputWrapper = styled.div`
  margin: 20px 0;
  position: relative;
  width: 100%;
`;
