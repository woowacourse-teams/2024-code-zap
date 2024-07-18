import styled from '@emotion/styled';

export const SnippetEditorContainer = styled.div`
  overflow: hidden;
  width: 100%;
  height: 100%;
  border-radius: 8px;
`;

export const SnippetFileNameInput = styled.input`
  padding: 1rem 1.5rem;
  border: none;
  width: 100%;
  height: 3rem;
  background-color: #393e46;
  font-size: 14px;
  font-weight: 700;
  color: #ffd369;

  &:focus {
    border-bottom: 2px solid #00adb5;
    outline: none;
  }
`;
