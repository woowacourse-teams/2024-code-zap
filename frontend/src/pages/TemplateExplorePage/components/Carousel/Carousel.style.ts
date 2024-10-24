import styled from '@emotion/styled';

import { ChevronIcon } from '@/assets/images';

export const CarouselContainer = styled.div`
  position: relative;

  display: flex;
  gap: 1rem;
  align-items: center;

  width: 100%;
  padding: 1rem;
`;

export const CarouselViewport = styled.div`
  scroll-behavior: smooth;
  scrollbar-width: none;

  position: relative;

  overflow-x: scroll;

  width: 100%;
  padding-right: 1px;

  &::-webkit-scrollbar {
    display: none;
  }
`;

export const CarouselList = styled.ul`
  display: flex;
  gap: 0.75rem;
  width: max-content;
  padding: 0.5rem;
`;

export const CarouselItem = styled.li`
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;

  width: 18.75rem;
  height: 9rem;

  @media (max-width: 768px) {
    width: 9rem;
  }
`;

export const PrevIcon = styled(ChevronIcon)`
  cursor: 'pointer';
  transform: rotate(90deg);
  flex-shrink: 0;
`;

export const NextIcon = styled(ChevronIcon)`
  cursor: 'pointer';
  transform: rotate(270deg);
  flex-shrink: 0;
`;
