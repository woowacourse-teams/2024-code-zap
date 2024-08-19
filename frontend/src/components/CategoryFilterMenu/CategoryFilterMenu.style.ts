import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const CategoryContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
`;

export const CategoryListContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

export const CategoryButtonContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 12.5rem;
`;

export const CategoryButtonWrapper = styled.button`
  cursor: pointer;

  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 100%;
  height: 3rem;
  padding: 0.5rem;

  text-overflow: ellipsis;
  white-space: nowrap;

  background-color: white;
  border-radius: 8px;
  box-shadow: 1px 2px 8px #00000020;
`;

export const IconButtonWrapper = styled.button`
  cursor: pointer;

  align-self: flex-end;

  padding: 0;

  opacity: 0.7;
  background: none;
  border: none;

  &:hover {
    transform: scale(1.15);
    opacity: 1;
  }
`;

export const HighlightBox = styled.div<{ selectedIndex: number; categoryCount: number }>`
  position: absolute;
  top: ${({ selectedIndex }) => `calc(${selectedIndex} * 3.75rem)`};
  left: 0;

  width: 12.5rem;
  height: 3rem;

  border: 3px solid ${theme.color.light.primary_500};
  border-radius: 8px;

  transition: top 0.25s ease-out;
`;
