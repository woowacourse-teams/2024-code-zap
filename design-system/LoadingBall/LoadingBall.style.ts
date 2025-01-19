import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

const bounce1 = keyframes`
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-75%);
  }
`;

const bounce2 = keyframes`
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-75%);
  }
`;

const bounce3 = keyframes`
   0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-75%);
  }
`;

export const LoadingBallContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  width: 75%;
  height: 100%;
  margin: auto;
`;

export const LoadingBallWrapper = styled.div`
  display: flex;
  gap: 1rem;
  justify-content: space-evenly;

  width: 100%;
  margin: auto;
`;

export const Ball = styled.li`
  width: 0.75rem;
  height: 0.75rem;

  list-style: none;

  background-color: ${theme.color.light.primary_500};
  border-radius: 50%;

  &:nth-of-type(1) {
    animation: ${bounce1} 0.6s ease-in-out infinite;
  }

  &:nth-of-type(2) {
    animation: ${bounce2} 0.6s 0.2s ease-in-out infinite;
  }

  &:nth-of-type(3) {
    animation: ${bounce3} 0.6s 0.4s ease-in-out infinite;
  }
`;
