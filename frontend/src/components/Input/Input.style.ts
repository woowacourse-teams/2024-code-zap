import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@/style/theme';
import type { BaseProps, TextFieldProps } from './Input';

const sizes = {
  small: css`
    gap: 0.5rem;

    height: 2rem;
    padding: 0 0.75rem;

    font-size: 0.75rem;
    line-height: 100%;

    border-radius: 8px;
  `,
  medium: css`
    gap: 0.875rem;

    height: 2.5rem;
    padding: 0 0.875rem;

    font-size: 0.875rem;
    line-height: 100%;

    border-radius: 10px;
  `,
  large: css`
    gap: 0.75rem;

    height: 3rem;
    padding: 0 1rem;

    font-size: 1rem;
    line-height: 100%;

    border-radius: 12px;
  `,
  xlarge: css`
    gap: 0.75rem;

    height: 4rem;
    padding: 1rem;

    font-size: 2rem;
    line-height: 100%;

    border-radius: 12px;
  `,
};

const variants = (color: string | undefined) => ({
  filled: css`
    background-color: ${color ?? '#ffffff'};
  `,
  outlined: css`
    background: none;
    border: 1px solid ${color ?? theme.color.light.tertiary_400};
  `,
  text: css`
    background: none;
  `,
});

export const Container = styled.div`
  cursor: text;

  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  width: 100%;
`;

export const Base = styled.div<BaseProps>`
  display: flex;
  align-items: center;

  ${({ variant, inputColor }) => variant && variants(inputColor)[variant]};
  ${({ size }) => size && sizes[size]};
  ${({ isValid }) =>
    isValid === false &&
    css`
      border: 1px solid ${theme.color.light.analogous_primary_400};
    `};
`;

export const TextField = styled.input<TextFieldProps>`
  width: 100%;
  font-size: inherit;
  line-height: inherit;
  background: none;

  &::placeholder {
    color: ${({ placeholderColor }) => (placeholderColor ? placeholderColor : theme.color.light.tertiary_400)};
  }

  &:focus {
    outline: none;
  }

  &:disabled {
    cursor: default;
    opacity: 0.6;
    background: none;
    border-color: #ddd;
  }
`;

export const Label = styled.label`
  font-size: 1rem;
  line-height: 100%;
`;

export const Adornment = styled.div<BaseProps>`
  display: flex;

  ${({ size }) =>
    size === 'small' &&
    css`
      width: 0.75rem;
      height: 0.75rem;
    `}

  ${({ size }) =>
    size === 'medium' &&
    css`
      width: 1rem;
      height: 1rem;
    `}
    
  ${({ size }) =>
    size === 'large' &&
    css`
      width: 1.25rem;
      height: 1.25rem;
    `}
    
  ${({ size }) =>
    size === 'xlarge' &&
    css`
      width: 1.5rem;
      height: 1.5rem;
    `}
`;

export const HelperText = styled.span`
  height: 1rem;
  margin-left: 0.5rem;
  font-size: 1rem;
  color: ${theme.color.light.analogous_primary_400};
`;
