import styled from '@emotion/styled';

import { ChevronIcon } from '@/assets/images';
import { theme } from '@/style/theme';

export const CarouselContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;

  width: 100%;
  padding: 1rem;
`;

export const CarouselViewport = styled.div`
  position: relative;
  overflow: hidden;
  width: 100%;
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

  @media (max-width: 768px) {
    width: 9rem;
  }
`;

export const CarouselButton = styled.button`
  cursor: pointer;

  padding: 1rem;

  background-color: white;
  border: 1px solid ${theme.color.light.secondary_300};
  border-radius: 8px;

  &:hover {
    background-color: ${theme.color.light.secondary_50};
  }
`;

export const PrevIcon = styled(ChevronIcon)`
  transform: rotate(90deg);
`;

export const NextIcon = styled(ChevronIcon)`
  transform: rotate(270deg);
`;
