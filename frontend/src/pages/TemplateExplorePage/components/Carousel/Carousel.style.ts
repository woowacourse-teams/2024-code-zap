import styled from '@emotion/styled';

import { ChevronIcon } from '@/assets/images';

export const CarouselContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;

  width: 100%;
  padding: 1rem;
`;

export const CarouselViewport = styled.div`
  scrollbar-width: none;

  position: relative;

  overflow: hidden;
  overflow-x: scroll;

  width: 100%;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const CarouselList = styled.ul<{ translateX: number; transitioning: boolean }>`
  transform: translateX(${(props) => props.translateX}px);
  display: flex;
  gap: 1rem;
  transition: ${(props) => (props.transitioning ? 'transform 0.3s ease-in-out' : 'none')};
`;

export const CarouselItem = styled.li`
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;

  width: 18rem;
  height: 9rem;
  margin: 0.25rem 0;

  @media (max-width: 768px) {
    width: 9rem;
  }
`;

export const PrevIcon = styled(ChevronIcon)`
  cursor: pointer;
  transform: rotate(90deg);
`;

export const NextIcon = styled(ChevronIcon)`
  cursor: pointer;
  transform: rotate(270deg);
`;
