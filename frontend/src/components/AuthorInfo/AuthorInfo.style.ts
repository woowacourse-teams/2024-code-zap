import styled from '@emotion/styled';

import { theme } from '@/style/theme';

export const AuthorInfoContainer = styled.div`
  cursor: pointer;

  display: flex;
  gap: 0.25rem;
  align-items: center;

  box-sizing: border-box;
  min-width: 0;
  height: 100%;
  padding: 3px 5px;

  border-radius: 4px;

  &:hover {
    background-color: ${theme.color.light.primary_50};
  }
`;

export const EllipsisTextWrapper = styled.span`
  overflow: hidden;
  display: block;

  min-width: 0;

  text-overflow: ellipsis;
  white-space: nowrap;
`;
