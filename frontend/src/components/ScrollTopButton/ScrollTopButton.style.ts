import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const ScrollTopButton = styled.button`
  cursor: pointer;

  position: fixed;
  right: 2rem;
  bottom: 2rem;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 0.75rem;

  background-color: ${theme.color.light.primary_500};
  border: none;
  border-radius: 100%;
`;
