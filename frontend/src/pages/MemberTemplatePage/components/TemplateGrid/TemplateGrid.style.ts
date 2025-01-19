import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const TemplateContainer = styled.div<{ cols: number }>`
  display: grid;
  grid-gap: 1rem;
  grid-template-columns: repeat(${({ cols }) => cols}, minmax(0, 1fr));

  width: 100%;
  max-width: 63.125rem;
`;

export const TemplateCardWrapper = styled.div<{ isSelected: boolean }>`
  cursor: pointer;
  position: relative;
  width: 100%;
  border-radius: 8px;

  &::before {
    content: '';

    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;

    background-color: ${({ isSelected }) => (isSelected ? 'none' : 'rgba(255, 255, 255, 0.6)')};
    border-radius: 8px;
    box-shadow: ${({ isSelected }) => (isSelected ? `inset 0 0 0 3px ${theme.color.light.primary_500}` : 'none')};

    transition: box-shadow 0.2s ease-in-out;
  }

  &:hover::before {
    background-color: ${({ isSelected }) => (isSelected ? 'none' : 'rgba(255, 255, 255, 0.1)')};
  }
`;

export const NonInteractiveWrapper = styled.div`
  pointer-events: none;
`;
