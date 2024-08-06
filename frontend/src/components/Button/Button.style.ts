import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@/style/theme';
import type { Props } from './Button';

const variants = {
  contained: css`
    color: ${theme.mode === 'light' ? theme.color.light.white : theme.color.dark.secondary_800};
    background: ${theme.mode === 'light' ? theme.color.light.primary_800 : theme.color.light.primary_300};
    border: none;
  `,
  outlined: css`
    color: ${theme.mode === 'light' ? theme.color.light.primary_800 : theme.color.light.primary_300};
    background: none;
    border: solid;
    border-color: ${theme.mode === 'light' ? theme.color.light.primary_800 : theme.color.light.primary_300};
  `,
  text: css`
    color: ${theme.mode === 'light' ? theme.color.light.primary_800 : theme.color.light.primary_300};
    background: none;
    border: none;
  `,
};

const sizes = {
  small: css`
    gap: 0.25rem;
    height: 1.75rem;
    padding: 0 0.5rem;
    font-size: ${theme.font.size.text.xsmall};
  `,
  medium: css`
    gap: 0.5rem;
    height: 2.375rem;
    padding: 0 1.5rem;
    font-size: ${theme.font.size.text.small};
  `,
};

const weights = {
  regular: css`
    font-weight: ${theme.font.weight.regular};
    border-width: 1px;
  `,
  bold: css`
    font-weight: ${theme.font.weight.bold};
    border-width: 2px;
  `,
};

const hoverStyles = {
  none: css``,
  base: css`
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
  `,
};

export const Button = styled.button<Props>`
  cursor: pointer;

  display: flex;
  align-items: center;
  justify-content: center;

  text-align: center;

  border-radius: 8px;

  ${({ size }) => size && sizes[size]};
  ${({ variant }) => variant && variants[variant]};
  ${({ weight }) => weight && weights[weight]};
  ${({ fullWidth }) =>
    fullWidth &&
    css`
      width: 100%;
    `};

  ${({ hoverStyle }) => hoverStyle && hoverStyles[hoverStyle]}
`;
