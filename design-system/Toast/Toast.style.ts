import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

const slideIn = keyframes`
  from {
    transform: translateY(20px) translateX(-50%);
    opacity: 0;
  }
  to {
    transform: translateY(0) translateX(-50%);
    opacity: 1;
  }
`;

const slideOut = keyframes`
  from {
    transform: translateY(0) translateX(-50%);
    opacity: 1;
  }
  to {
    transform: translateY(20px) translateX(-50%);
    opacity: 0;
  }
`;

export const BaseToast = styled.div<{
  visible: boolean;
  type: 'success' | 'fail' | 'info';
}>`
  position: fixed;
  z-index: 1000;
  top: 6rem;
  left: 50%;
  transform: translateX(-50%);

  display: inline-block;

  padding: 16px;

  font-size: 16px;
  color: white;

  background-color: ${({ type }) => {
    switch (type) {
      case 'success':
        return theme.color.light.complementary_300;

      case 'fail':
        return theme.color.light.analogous_primary_300;

      case 'info':
        return theme.color.light.primary_400;

      default:
        return theme.color.light.primary_400;
    }
  }};
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  animation: ${({ visible }) => (visible ? slideIn : slideOut)} 0.5s ease-out;
  animation-fill-mode: forwards;
`;
