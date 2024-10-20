import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

const getAnimation = (state: string, moveDirection: string) => keyframes`
  from {
    transform: ${{ left: 'translateX(calc(100% + 3rem))', right: 'translateX(calc(-100% - 3rem))' }[moveDirection]} ${
      (state === `${moveDirection}Outside` && 'scale(1)') ||
      (state === { left: 'rightInside', right: 'leftInside' }[moveDirection] && 'scale(0.75)')
    };
    opacity: ${
      (state === `${moveDirection}Outside` && 0.99) ||
      (state === { left: 'rightInside', right: 'leftInside' }[moveDirection] && 0)
    };
  }
  to {
    transform: ${{ left: 'translateX(0)', right: 'translateX(0)' }[moveDirection]} ${
      (state === { left: 'rightInside', right: 'leftInside' }[moveDirection] && 'scale(1)') ||
      (state === `${moveDirection}Outside` && 'scale(0.75)')
    };
    opacity: ${
      (state === { left: 'rightInside', right: 'leftInside' }[moveDirection] && 0.99) ||
      (state === `${moveDirection}Outside` && 0)
    };
  }
`;

export const CarouselItemSlotWrapper = styled.div<{
  duration: number;
  moveDirection: string;
  state: string;
}>`
  cursor: ${({ state }) => (['leftOutside', 'rightOutside'].includes(state) ? 'auto' : 'pointer')};
  user-select: ${({ state }) => ['leftOutside', 'rightOutside'].includes(state) && 'none'};

  transform-origin: ${({ state }) => `${{ leftOutside: 'right', rightOutside: 'left' }[state]}`};

  overflow: clip;
  display: flex;

  width: 12.5rem;
  height: 12.5rem;

  opacity: ${({ moveDirection, state }) =>
    (moveDirection === 'static' && ['leftOutside', 'rightOutside'].includes(state)) ||
    (moveDirection === 'left' && ['rightInside', 'rightOutside'].includes(state)) ||
    (moveDirection === 'right' && ['leftInside', 'leftOutside'].includes(state))
      ? 0
      : 0.99};
  background: white;
  border-radius: 16px;
  box-shadow: 1px 2px 8px 1px #00000020;

  transition: transform 0.2s;
  animation: ${({ state, moveDirection }) => moveDirection !== 'static' && getAnimation(state, moveDirection)}
    ${({ duration }) => duration}s ease-out forwards;

  &:hover {
    transform: scale(1.05);
    box-shadow: 1px 2px 8px 1px #00000030;
  }
`;
