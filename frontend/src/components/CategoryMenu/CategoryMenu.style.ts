import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const CategoryContainer = styled.div`
  --button_height: 3rem;
  --button_gap: 0.75rem;

  position: relative;
  display: flex;
  flex-direction: column;
  gap: var(--button_gap);
`;

export const CategoryButtonWrapper = styled.button`
  cursor: pointer;

  position: relative;

  width: 12.5rem;
  height: var(--button_height);

  background-color: white;
  border-radius: 8px;
  box-shadow: 1px 2px 8px #00000020;
`;

export const HighlightBox = styled.div<{ selectedIndex: number; categoryCount: number }>`
  cursor: pointer;

  position: absolute;
  top: ${({ selectedIndex }) => `calc(${selectedIndex} * (var(--button_height) + var(--button_gap)))`};
  left: 0;

  width: 12.5rem;
  height: 3rem;

  border: 3px solid ${theme.color.light.primary_600};
  border-radius: 8px;

  transition: top 0.25s ease-out;
`;
