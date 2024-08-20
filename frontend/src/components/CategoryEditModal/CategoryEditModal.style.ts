import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const EditCategoryItemList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
`;

export const EditCategoryItem = styled.div<{ hasError?: boolean }>`
  display: flex;
  gap: 1rem;
  align-items: center;

  padding: 0.25rem 1rem;

  border: ${({ hasError }) => (hasError ? '1px solid red' : 'none')};
  border-radius: 8px;

  &:hover {
    outline: 1px solid ${theme.color.light.secondary_500};
    outline-offset: -2px;
  }
`;

export const IconButtonContainer = styled.div`
  display: flex;
  gap: 0.5rem;
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
