import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const TagFilterMenuContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  align-items: flex-start;

  width: 12.5rem;
  padding: 1rem;

  border: 1px solid ${theme.color.light.secondary_300};
  border-radius: 8px;
`;
