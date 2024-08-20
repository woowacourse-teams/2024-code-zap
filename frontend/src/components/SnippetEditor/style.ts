import styled from '@emotion/styled';

export const SnippetEditorContainer = styled.div`
  overflow: hidden;
  width: 100%;
  height: 100%;
  border-radius: 8px;
`;

export const SnippetFileNameInput = styled.input`
  width: 100%;
  height: 3rem;
  padding: 1rem 1.5rem;

  font-size: 14px;
  font-weight: 700;
  color: #ffd369;

  background-color: #393e46;
  border: none;

  &:focus {
    border-bottom: 2px solid #00adb5;
    outline: none;
  }
`;
