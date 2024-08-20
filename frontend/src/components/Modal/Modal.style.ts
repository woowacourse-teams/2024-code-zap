import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { ModalSize } from './Modal';

export const Container = styled.div`
  position: fixed;
  z-index: 200;
  top: 0;
  left: 0;

  display: flex;
  align-items: center;
  justify-content: center;

  width: 100vw;
  height: 100vh;

  @media (max-width: 47.9375rem) {
    align-items: flex-end;
  }
`;

export const Backdrop = styled.div`
  position: absolute;
  z-index: 201;
  top: 0;
  left: 0;

  width: 100vw;
  height: 100vh;

  background-color: rgba(0, 0, 0, 0.3);
`;

export const Header = styled.div`
  margin-bottom: 1rem;
  font-size: 1.25rem;
  font-weight: bold;
`;

export const Body = styled.div`
  margin-bottom: 1.5rem;
  font-size: 1rem;
`;

export const Footer = styled.div`
  display: flex;
  gap: 1rem;
  justify-content: space-between;
`;

export const Base = styled.div<{ size: ModalSize }>`
  position: relative;
  z-index: 202;

  padding: 1.5rem;

  background-color: white;
  border-radius: 24px;

  @media (min-width: 48rem) {
    position: fixed;
    padding: 1.5rem;
    background-color: white;

    ${({ size }) => size && sizes[size]};
  }

  @media (max-width: 47.9375rem) {
    bottom: 0;
    left: 0;

    overflow-y: auto;

    width: 100%;
    max-height: 90vh;

    border-radius: 1.5rem 1.5rem 0 0;
  }
`;

const sizes = {
  xsmall: css`
    width: 90%;
    max-width: 17.5rem;
    min-height: 11.25rem;
  `,
  small: css`
    width: 90%;
    max-width: 25rem;
    min-height: 18.75rem;
  `,
  medium: css`
    width: 90%;
    max-width: 37.5rem;
    min-height: 28.125rem;
  `,
  large: css`
    width: 90%;
    max-width: 50rem;
    min-height: 37.5rem;
  `,
};
