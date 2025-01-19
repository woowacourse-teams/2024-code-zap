import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const LikeButtonContainer = styled.button<{ isLiked: boolean }>`
  cursor: pointer;

  display: flex;
  align-items: center;
  justify-content: space-between;

  min-width: 5.5rem;
  height: 2.25rem;
  padding: 0 1rem;

  color: ${({ isLiked }) =>
    isLiked ? theme.color.light.analogous_primary_400 : 'white'};

  border: 1.5008px solid
    ${({ isLiked }) =>
      isLiked
        ? theme.color.light.analogous_primary_400
        : theme.color.light.secondary_800};
  border-radius: 24px;
`;
