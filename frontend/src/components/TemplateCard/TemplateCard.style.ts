import styled from '@emotion/styled';
import { EditorView } from '@uiw/react-codemirror';

import { theme } from '@/style/theme';

export const TemplateCardContainer = styled.div`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  box-sizing: border-box;
  width: 100%;
  padding: 1.5rem;

  background: ${theme.color.light.white};
  border-radius: 8px;
  box-shadow: 1px 2px 8px 1px #00000020;

  transition: 0.1s ease;

  &:hover {
    bottom: 0.5rem;
    transform: scale(1.025);
    box-shadow: 1px 2px 8px 1px #00000030;
  }
`;

export const TagListContainer = styled.div`
  overflow: hidden;
  display: flex;
  flex-wrap: nowrap;
  gap: 0.5rem;

  height: 2.15rem;
`;

export const AllTagListModal = styled.div`
  position: relative;
  display: flex;
  border-radius: 8px;
`;

export const AllTagListContainer = styled.div`
  scrollbar-width: none;

  position: absolute;
  z-index: 1;
  top: -45px;

  overflow-y: scroll;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;

  width: 100%;
  height: max-content;
  max-height: 3.75rem;
  padding: 0.5rem 1rem;

  background-color: white;
  border: 1px solid ${theme.color.light.secondary_500};
  border-radius: 8px;

  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }
`;

export const EllipsisTextWrapper = styled.span`
  overflow: hidden;
  display: block;

  min-width: 0;

  text-overflow: ellipsis;
  white-space: nowrap;
`;

export const NoWrapTextWrapper = styled.div`
  white-space: nowrap;
`;

export const BlankDescription = styled.div`
  height: 1rem;
`;

export const CustomCodeMirrorTheme = EditorView.theme({
  '.cm-activeLine': { backgroundColor: `transparent !important` },
  '.cm-activeLineGutter': { backgroundColor: `transparent !important` },
});
