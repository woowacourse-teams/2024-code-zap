import styled from '@emotion/styled';

export const LoadingOverlay = styled.div`
  position: absolute;
  z-index: 1000;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;

  display: flex;
  align-items: flex-start;
  justify-content: center;

  padding-top: 1rem;

  background-color: rgba(255, 255, 255, 0.7);
`;

export const LoadingBallWrapper = styled.div`
  width: 100%;
  margin-top: 10rem;
`;
