import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { ModalSize } from './Modal';

export const Base = styled.div`
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
    height: 100%;
    min-height: 100vh;

    @supports (-webkit-touch-callout: none) {
      min-height: -webkit-fill-available;
    }
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

export const HeaderWrapper = styled.div`
  display: flex;
  flex-shrink: 0;
  align-items: center;
  padding: 1rem;
`;

export const BodyContainer = styled.div`
  overflow-y: auto;
  flex-grow: 1;
  padding: 1rem;
  font-size: 1rem;
`;

export const FooterContainer = styled.div`
  display: flex;
  flex-shrink: 0;
  gap: 1rem;
  justify-content: space-between;

  padding: 1rem;

  @media (max-width: 47.9375rem) {
    position: sticky;
    z-index: 1;
    bottom: 0;
    background-color: white;
  }
`;

export const ModalContainer = styled.div<{ size: ModalSize }>`
  position: relative;
  z-index: 202;

  display: flex;
  flex-direction: column;

  max-height: 90vh;
  padding: 1rem;

  background-color: white;
  border-radius: 24px;

  @media (min-width: 48rem) {
    position: fixed;
    width: 90%;
    ${({ size }) => size && sizes[size]};
  }

  @media (max-width: 47.9375rem) {
    position: sticky;
    bottom: 0;
    left: 0;

    overflow-y: auto;

    width: 100%;
    max-height: calc(100% - 2rem);
    margin-top: auto;

    border-radius: 1.5rem 1.5rem 0 0;

    transition: max-height 0.3s ease-out;
  }
`;

const sizes = {
  xsmall: css`
    max-width: 18.5rem;
    min-height: 11.25rem;
  `,
  small: css`
    max-width: 25rem;
    min-height: 18.75rem;
  `,
  medium: css`
    max-width: 37.5rem;
    min-height: 28.125rem;
  `,
  large: css`
    max-width: 50rem;
    min-height: 37.5rem;
  `,
};
