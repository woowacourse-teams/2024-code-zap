import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const StyledPagingButton = styled.button`
  cursor: pointer;

  width: 2rem;
  height: 2rem;

  color: ${theme.color.light.secondary_500};

  background: none;
  border: none;
  border-radius: 8px;

  :disabled {
    background-color: ${theme.color.light.primary_500};

    span {
      color: ${theme.color.light.white};
    }
  }

  :not(:disabled):hover span {
    color: ${theme.color.light.secondary_700};
  }
`;
