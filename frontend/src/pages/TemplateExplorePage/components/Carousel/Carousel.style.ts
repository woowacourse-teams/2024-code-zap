import styled from '@emotion/styled';

export const CarouselContainer = styled.div`
  position: relative;

  display: flex;
  gap: 3rem;
  align-items: center;
  justify-content: center;

  width: 100%;
`;

export const MoveButton = styled.button<{ position: 'left' | 'right' }>`
  ${({ position }) => position}:0;

  cursor: pointer;
  user-select: none;

  position: absolute;
  z-index: 1;
  transform: rotateZ(${({ position }) => (position === 'left' ? '-90' : '90')}deg);

  padding: 0.25rem;
`;

export const CarouselItemSlotsContainer = styled.div`
  overflow-x: clip;
  display: flex;
  gap: 3rem;
  align-items: center;
  justify-content: center;
`;
