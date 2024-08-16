import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const TemplateContainer = styled.div<{ cols: number }>`
  display: grid;
  grid-gap: 1rem;
  grid-template-columns: repeat(${({ cols }) => cols}, minmax(0, 1fr));

  width: 100%;
  max-width: 63.125rem;
`;

export const TemplateCardWrapper = styled.div<{ isSelected: boolean }>`
  position: relative;
  width: 100%;
  border-radius: 8px;

  &::before {
    pointer-events: none;
    content: '';

    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;

    border-radius: 8px;
    box-shadow: ${(props) => (props.isSelected ? `inset 0 0 0 3px ${theme.color.light.primary_500}` : 'none')};

    transition: box-shadow 0.2s ease-in-out;
  }
`;

export const NonInteractiveWrapper = styled.div`
  pointer-events: none;
`;
