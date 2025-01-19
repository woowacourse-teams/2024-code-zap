import styled from '@emotion/styled';

import { Button } from '@/components';
import { theme } from '@design/style/theme';

export const SourceCodeViewerContainer = styled.div`
  overflow: hidden;
  width: 100%;
  border-radius: 8px;
`;

export const FilenameContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 100%;
  height: 3rem;
  padding: 1rem 1.5rem;

  background: ${theme.color.light.tertiary_600};
`;

export const ToggleButton = styled.button`
  cursor: pointer;

  display: flex;
  flex: 1;
  gap: 0.5rem;
  align-items: center;
  justify-content: space-between;

  min-width: 0;
`;

export const SourceCodeToggleIcon = styled.span<{ isOpen: boolean }>`
  transform: ${({ isOpen }) => (isOpen ? 'rotate(180deg)' : 'rotate(0deg)')};
  width: 16px;
  height: 16px;
  transition: transform 0.3s ease;
`;

export const NoScrollbarContainer = styled.div`
  scrollbar-width: none;

  overflow: auto;

  width: 100%;

  text-align: start;

  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const CopyButton = styled(Button)`
  width: auto;
  min-width: fit-content;
  padding: 0.25rem 0.5rem;
  white-space: nowrap;
`;

export const SourceCodeWrapper = styled.div<{ isOpen: boolean }>`
  overflow: scroll;
  max-height: ${({ isOpen }) => (isOpen ? '1000rem' : '0')};
  animation: ${({ isOpen }) => (!isOpen ? 'collapse' : 'expand')} 0.7s
    ease-in-out forwards;
`;
