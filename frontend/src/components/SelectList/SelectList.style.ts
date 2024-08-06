import styled from '@emotion/styled';

import { theme } from '@/style/theme';
import type { OptionProps } from './SelectList';

export const SelectListContainer = styled.div`
  --select-list-background-color: ${theme.color.light.secondary_100};

  position: relative;

  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  border-radius: 8px;

  transition: all 0.2s ease-in-out;

  &:hover {
    gap: 0;
    background: var(--select-list-background-color);
  }

  &:hover a {
    width: 11rem;
    height: 3rem;
    padding: 0 0.8rem;
    border-radius: 8px;
  }

  &:hover .select-list-text {
    margin-left: 1rem;
    opacity: 1;
    transition: all 0.3s ease-in-out;
  }

  @media (min-width: 80rem) {
    gap: 0;
    background: var(--select-list-background-color);
    a {
      width: 11rem;
      height: 3rem;
      padding: 0 0.8rem;
      border-radius: 8px;
    }

    .select-list-text {
      margin-left: 1rem;
      opacity: 1;
      transition: all 0.3s ease-in-out;
    }
  }
`;

export const SelectListOption = styled.a<OptionProps>`
  display: flex;
  align-items: center;

  width: 1rem;
  height: 1rem;

  text-decoration: none;

  background-color: ${({ isSelected }) =>
    isSelected ? theme.color.light.primary_500 : 'var(--select-list-background-color)'};
  border-radius: 50%;

  transition: all 0.2s ease-in-out;

  &:hover {
    background: ${({ isSelected }) => isSelected || `rgba(${hexToRgb(theme.color.light.secondary_400)}, 0.1)`};
  }
`;

export const SelectListText = styled.div`
  overflow: hidden;
  display: block;

  text-overflow: ellipsis;
  white-space: nowrap;

  opacity: 0;

  transition: all 0.1s ease-in-out;
`;

const hexToRgb = (hex: string) => {
  const shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;

  hex = hex.replace(shorthandRegex, (_, r, g, b) => r + r + g + g + b + b);
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);

  return result ? `${parseInt(result[1], 16)}, ${parseInt(result[2], 16)}, ${parseInt(result[3], 16)}` : null;
};
