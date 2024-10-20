import styled from '@emotion/styled';

export const CarouselItemWrapper = styled.div<{ background: string }>`
  display: flex;
  align-items: center;
  justify-content: center;

  width: 100%;
  height: 100%;

  object-fit: cover;
  background: ${({ background }) => background};
`;
