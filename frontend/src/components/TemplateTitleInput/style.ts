import styled from '@emotion/styled';

export const TemplateTitleInput = styled.input`
  width: 100%;
  padding: 10px 0;

  font-size: 16px;
  color: #ccc;

  background: none;
  border: none;
  border-bottom: 1px solid #555;

  &::placeholder {
    color: #808080;
  }

  &:focus {
    border-bottom: 1px solid #ccc;
    outline: none;
  }
`;

export const InputWrapper = styled.div`
  position: relative;
  width: 100%;
  margin: 20px 0;
`;
