import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const PagingContainer = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const PagingButton = styled.button<{ isActive?: boolean }>`
  cursor: pointer;

  width: 2rem;
  height: 2rem;

  color: ${theme.color.light.secondary_500};

  background: none;
  border: none;
  border-radius: 8px;

  ${({ isActive }) =>
    isActive &&
    `
    background-color: ${theme.color.light.primary_500};
    color: ${theme.color.light.white};
    cursor: default;
  `}

  &:disabled {
    cursor: default;
    opacity: ${({ isActive }) => (isActive ? 1 : 0.7)};
  }

  &:not(:disabled):hover span {
    color: ${theme.color.light.secondary_700};
  }
`;
