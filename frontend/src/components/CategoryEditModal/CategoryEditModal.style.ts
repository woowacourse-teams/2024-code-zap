import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { theme } from '../../style/theme';

export const EditCategoryItemList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
`;

export const EditCategoryItem = styled.div<{ hasError?: boolean; isButton?: boolean; disabled?: boolean }>`
  display: flex;
  gap: 1rem;
  align-items: center;

  height: 3rem;
  padding: ${({ isButton }) => (isButton ? '0' : '0 1rem')};

  border: ${({ hasError }) => (hasError ? `1px solid red` : 'none')};
  border-radius: 8px;
  box-shadow: 1px 1px 4px #00000030;

  transition: all 0.1s ease-out;

  ${({ disabled }) =>
    !disabled &&
    css`
      &:hover,
      &:focus-within {
        transform: scale(1.015);
        box-shadow: 1px 1px 2px 1px #00000040;
      }
    `}

  ${({ disabled }) =>
    disabled &&
    css`
      button {
        cursor: default;
        opacity: 0.7;
      }
    `}

  &:hover span {
    color: ${theme.color.light.secondary_700};
  }

  &:hover button,
  &:focus-within button {
    visibility: visible;
  }

  &:focus-within div {
    padding: 0;
    font-weight: bold;
    color: ${theme.color.light.secondary_700};
  }
`;

export const IconButtonContainer = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const IconButtonWrapper = styled.button`
  cursor: pointer;

  align-self: flex-end;

  padding: 0;

  visibility: hidden;
  opacity: 0.7;
  background: none;
  border: none;

  &:hover {
    transform: scale(1.15);
    opacity: 1;
  }
`;
