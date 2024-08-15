import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const CategoryContainer = styled.div`
  --button_height_D: 3rem;
  --button_height_M: 3rem;
  --button_gap_D: 0.75rem;
  --button_gap_M: 0rem;

  position: relative;

  display: flex;
  flex-direction: column;
  gap: var(--button_gap_D);

  transition: left 0.25s ease-out;

  @media (max-width: 768px) {
    position: fixed;
    left: -11rem;
    gap: 0;

    &:hover {
      left: 0;
    }
  }
`;

export const CategoryButtonWrapper = styled.button`
  cursor: pointer;

  position: relative;

  overflow: hidden;

  width: 12.5rem;
  height: var(--button_height_D);
  padding: 0.5rem;

  text-overflow: ellipsis;
  white-space: nowrap;

  background-color: white;
  border-radius: 8px;
  box-shadow: 1px 2px 8px #00000020;

  @media (max-width: 768px) {
    height: var(--button_height_M);
  }
`;

export const HighlightBox = styled.div<{ selectedIndex: number; categoryCount: number }>`
  cursor: pointer;

  position: absolute;
  top: ${({ selectedIndex }) => `calc(${selectedIndex} * (var(--button_height_D) + var(--button_gap_D)))`};
  left: 0;

  width: 12.5rem;
  height: var(--button_height_D);

  border: 3px solid ${theme.color.light.primary_500};
  border-radius: 8px;

  transition: top 0.25s ease-out;

  @media (max-width: 768px) {
    top: ${({ selectedIndex }) => `calc(${selectedIndex} * (var(--button_height_M) + var(--button_gap_M)))`};
    height: var(--button_height_M);
  }
`;
