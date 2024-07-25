import styled from '@emotion/styled';

export const SnippetEditorContainer = styled.div`
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 8px;
`;

export const SnippetFileNameInput = styled.input`
  width: 100%;
  height: 3rem;
  background-color: #393e46;
  border: none;
  padding: 1rem 1.5rem;
  color: #ffd369;
  font-size: 14px;
  font-weight: 700;

  &:focus {
    outline: none;
    border-bottom: 2px solid #00adb5;
  }
`;
