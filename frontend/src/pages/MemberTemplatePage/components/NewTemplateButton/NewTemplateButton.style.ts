import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const NewTemplateButtonWrapper = styled.button`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
  justify-content: center;

  width: 100%;
  height: 19.75rem;
  padding: 1.5rem;

  border: 3px dashed ${theme.color.light.primary_500};
  border-radius: 8px;
  &:hover {
    background-color: ${theme.color.light.primary_50};
  }
`;
