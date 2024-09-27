import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import { EditorView } from '@uiw/react-codemirror';

import { Button } from '@/components';

const expand = keyframes`
  from {
    max-height: 0;
  }
  to {
    max-height: 1000rem;
  }
`;

const collapse = keyframes`
  from {
    max-height: 1000rem;
  }
  to {
    max-height: 0;
  }
`;

export const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  width: 100%;
  max-width: 53rem;
  margin: auto;
  margin-top: 3rem;
`;

export const SidebarContainer = styled.aside`
  position: fixed;
  top: 20rem;
  right: 1.5rem;
  margin-left: -10rem;

  @media (min-width: 80rem) {
    position: sticky;
    top: 20rem;
    right: 0;
    align-self: flex-start;
  }
`;

export const SyntaxHighlighterWrapper = styled.div<{ isOpen: boolean }>`
  overflow: hidden;
  max-height: ${({ isOpen }) => (isOpen ? '1000rem' : '0')};
  animation: ${({ isOpen }) => (!isOpen ? collapse : expand)} 0.7s ease-in-out forwards;
`;

export const NoScrollbarContainer = styled.div`
  scrollbar-width: none;
  overflow: auto;
  width: 100%;

  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const EditButton = styled(Button)`
  padding: 0;
`;

export const DeleteButton = styled(Button)`
  padding: 0;
`;

export const CustomCodeMirrorTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `transparent !important` },
  '.cm-activeLineGutter': { backgroundColor: `transparent !important` },
});
