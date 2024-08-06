import styled from '@emotion/styled';

import { theme } from '../../style/theme';

export const StyledPagingButton = styled.button`
  cursor: pointer;

  width: 1.5rem;
  height: 1.5rem;
  margin: 0 0.25rem;

  color: ${theme.color.light.secondary_500};

  background: none;
  border: none;

  :disabled {
    background-color: ${theme.color.light.primary_800};

    span {
      color: ${theme.color.light.white};
    }
  }

  :not(:disabled):hover span {
    color: ${theme.color.light.secondary_700};
  }
`;
