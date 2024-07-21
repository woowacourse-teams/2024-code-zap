import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { Props } from './Button';

type StyleProps = Pick<Props, 'variant' | 'size' | 'width'>;

const variants = {
  default: css`
    color: rgb(52 60 72 / 100%);
    background: rgb(255 211 105 / 100%);
  `,
  outlined: css`
    color: rgb(255 211 105 / 100%);
    background: none;
    border: 0.1rem solid rgb(255 211 105 / 100%);
  `,
  text: css`
    display: inline-block;

    width: fit-content;
    height: fit-content;
    padding: 0;

    color: rgb(255 211 105 / 100%);

    background: none;
    border: none;
  `,
};

const sizes = {
  small: css`
    height: 3rem;
    font-size: 1.4rem;
    font-weight: 700;
  `,
  medium: css`
    height: 4rem;
    font-size: 1.8rem;
    font-weight: 700;
  `,
};

export const Button = styled.button<StyleProps>`
  cursor: pointer;

  display: flex;
  gap: 0.8rem;
  align-items: center;

  width: ${({ width }) => width};
  padding: 0.8rem 1.6rem;

  text-align: center;

  border-radius: 8px;

  ${({ size = 'medium' }) => sizes[size]};
  ${({ variant = 'default' }) => variants[variant]};

  &:disabled {
    cursor: default;
    opacity: 0.6;
  }

  &:not(:disabled):focus {
    outline: none;
  }

  &:not(:disabled):hover {
    opacity: 0.8;
  }

  &:not(:disabled):active {
    opacity: 0.6;
  }
`;
