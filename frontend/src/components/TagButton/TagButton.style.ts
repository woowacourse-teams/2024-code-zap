import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const TagButtonWrapper = styled.button<{ isFocused: boolean }>`
  cursor: pointer;

  display: flex;
  gap: 0.5rem;
  align-items: center;
  justify-content: center;

  box-sizing: border-box;
  padding: 0.5rem 0.75rem;

  background-color: ${({ isFocused }) => (isFocused ? theme.color.light.primary_400 : theme.color.light.tertiary_50)};
  border: ${({ isFocused }) =>
    isFocused ? `2px solid ${theme.color.light.primary_600}` : `1px solid ${theme.color.light.tertiary_200}`};
  border-radius: 2.5rem;

  &:not(:disabled):hover {
    box-shadow: 0 1px 4px #00000030;
  }
`;
