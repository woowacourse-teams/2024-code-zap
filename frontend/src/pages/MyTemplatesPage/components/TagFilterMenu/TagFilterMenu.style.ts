import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const TagFilterMenuContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: flex-start;

  width: 100%;
  padding: 1rem;

  border: 1px solid ${theme.color.light.secondary_300};
  border-radius: 8px;
`;

export const TagButtonsContainer = styled.div<{ height: string }>`
  overflow: hidden;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  align-items: flex-start;

  width: 100%;
  height: ${({ height }) => height};

  transition: height 0.3s ease-in-out;
`;

export const ShowMoreButton = styled.button<{ size: number; isExpanded: boolean }>`
  cursor: pointer;

  display: flex;
  align-items: center;
  justify-content: center;

  width: ${({ size }) => `${size}rem`};
  height: ${({ size }) => `${size}rem`};

  transition: transform 0.3s ease-in-out;

  ${({ isExpanded }) =>
    isExpanded &&
    `
      transform: rotate(180deg);
    `}
`;
