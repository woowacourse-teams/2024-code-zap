import { css } from '@emotion/react';
import styled from '@emotion/styled';

import type { BaseProps, TextFieldProps } from './Input';

const sizes = {
  small: css`
    gap: 0.8rem;

    height: 3.2rem;
    padding: 1rem 1.2rem;

    font-size: 1.2rem;
    line-height: 100%;

    border-radius: '0.8rem';
  `,
  medium: css`
    gap: 1.2rem;

    height: 4.8rem;
    padding: 1.6rem;

    font-size: 1.6rem;
    line-height: '100%';

    border-radius: 1.2rem;
  `,
};

const variants = {
  filled: css`
    background-color: #ffffff;
  `,
  outlined: css`
    background: none;
    border: 0.1rem solid #808080;
  `,
  text: css`
    background: none;
  `,
};

export const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

export const Base = styled.div<BaseProps>`
  display: flex;
  align-items: center;

  ${({ variant }) => variant && variants[variant]};
  ${({ size }) => size && sizes[size]};
  ${({ isValid }) =>
    isValid === false &&
    css`
      border: 0.1rem solid red;
    `};

  /* for Adornment size */
  & > div {
    ${({ size }) =>
      size === 'small' &&
      css`
        width: 1.2rem;
        height: 1.2rem;
      `}

    ${({ size }) =>
      size === 'medium' &&
      css`
        width: 1.6rem;
        height: 1.6rem;
      `}
  }
`;

export const TextField = styled.input<TextFieldProps>`
  width: 100%;
  font-size: inherit;
  line-height: inherit;
  background: none;

  &::placeholder {
    color: #788496;
  }

  &:focus {
    outline: none;
    &::placeholder {
      color: transparent;
    }
  }

  &:disabled {
    cursor: default;
    opacity: 0.6;
    background: none;
    border-color: #ddd;
  }
`;

export const Adornment = styled.div`
  display: flex;
`;

export const HelperText = styled.span`
  margin-left: 8px;
  color: red;
`;
