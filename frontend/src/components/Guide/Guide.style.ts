import { keyframes, css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@/style/theme';

const slideUp = keyframes`
  from {
    transform: translateY(18px);
  }
  to {
    transform: translateY(0);
  }
`;

const slideDown = keyframes`
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(10px);
  }
`;

export const GuideContainer = styled.div<{ isOpen: boolean }>`
  display: flex;
  gap: 0.125rem;

  padding: 0.75rem;

  opacity: ${({ isOpen }) => (isOpen ? 1 : 0)};
  background-color: ${theme.color.light.complementary_50};
  border-radius: 12px;
  box-shadow: 1px 2px 8px 1px #00000030;

  transition:
    opacity 0.3s ease-out,
    transform 0.3s ease-out;

  ${({ isOpen }) =>
    isOpen
      ? css`
          animation: ${slideUp} 0.3s ease-out forwards;
        `
      : css`
          animation: ${slideDown} 0.3s ease-out forwards;
        `}

  @media (max-width: 768px) {
    flex-direction: column;
  }
`;
