import styled from '@emotion/styled';

export const SnippetEditorContainer = styled.div`
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 8px;
`;

export const SnippetTitleInput = styled.input`
  width: 100%;
  height: 3rem;
  background-color: #393e46;
  color: white;
  border: none;
  padding: 1rem;

  &:focus {
    outline: none;
    border-bottom: 2px solid #00adb5;
  }
`;
