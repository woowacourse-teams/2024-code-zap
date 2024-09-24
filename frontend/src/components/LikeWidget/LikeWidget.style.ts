import styled from '@emotion/styled';

import { theme } from '../../style/theme';

export const LikeButtonWidgetContainer = styled.div<{ isLiked: boolean; clickable: boolean }>`
  cursor: ${({ clickable }) => (clickable ? 'pointer' : 'default')};

  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 4.25rem;
  height: 1.5rem;
  padding: 0 0.5rem;

  border: 1px solid
    ${({ isLiked }) => (isLiked ? theme.color.light.analogous_primary_400 : theme.color.light.secondary_800)};
  border-radius: 1rem;
`;

export const LikeCount = styled.span``;
