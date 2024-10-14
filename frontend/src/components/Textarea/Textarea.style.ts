import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '@/style/theme';

import { BaseProps, TextFieldProps } from './Textarea';

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

const sizes = {
  small: css`
    padding: 8px;
    font-size: 14px;
    border-radius: 8px;
  `,
  medium: css`
    padding: 12px;
    font-size: 16px;
    border-radius: 10px;
  `,
  large: css`
    padding: 16px;
    font-size: 18px;
    border-radius: 12px;
  `,
  xlarge: css`
    padding: 16px;
    font-size: 32px;
    border-radius: 12px;
  `,
};

export const Container = styled.div`
  cursor: text;

  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  width: 100%;
`;

export const Base = styled.div<BaseProps>`
  display: flex;
  flex-direction: column;
  width: 100%;

  ${({ variant, inputColor }) => variant && variants(inputColor)[variant]};
  ${({ size = 'medium' }) => sizes[size]};
  ${({ isValid }) =>
    isValid === false &&
    css`
      border-color: ${theme.color.light.analogous_primary_400};
    `};
`;

export const TextareaField = styled.textarea<TextFieldProps>`
  resize: none;

  width: 100%;

  font-family: inherit;
  font-size: inherit;
  line-height: 1.5;

  background: none;
  border: none;
  outline: none;

  &::placeholder {
    color: ${({ placeholderColor }) => placeholderColor || theme.color.light.tertiary_400};
  }

  &:disabled {
    cursor: default;
    opacity: 0.6;
  }
`;

export const Label = styled.label`
  font-size: 1rem;
  line-height: 100%;
`;

export const HelperText = styled.span`
  height: 1rem;
  margin-left: 0.5rem;
  font-size: 1rem;
  color: ${theme.color.light.analogous_primary_400};
`;
