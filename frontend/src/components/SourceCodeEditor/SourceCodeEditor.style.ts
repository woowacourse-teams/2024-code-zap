import styled from '@emotion/styled';
import { EditorView } from '@uiw/react-codemirror';

import { theme } from '@/style/theme';

export const SourceCodeEditorContainer = styled.div`
  overflow: hidden;
  width: 100%;
  height: 100%;
  border-radius: 8px;
`;

export const SourceCodeFileNameInput = styled.input`
  width: 100%;
  height: 3rem;
  padding: 1rem 1.5rem;
  padding-right: 3rem;

  font-size: 16px;
  font-weight: 700;
  color: ${theme.color.dark.white};

  background-color: ${theme.color.light.tertiary_600};
  border: none;

  &::placeholder {
    color: rgba(255, 255, 255, 0.4);
  }

  &:focus {
    outline: none;
    box-shadow: inset 0 -3px 0 ${theme.color.light.triadic_primary_300};
  }
`;

export const CustomTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `rgba(0, 0, 0, 0.1) !important` },
  '.cm-activeLineGutter': { backgroundColor: `rgba(0, 0, 0, 0.1) !important` },
});
