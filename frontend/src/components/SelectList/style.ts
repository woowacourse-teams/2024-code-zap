import styled from '@emotion/styled';

import { theme } from '@/style/theme';
import type { OptionProps } from './SelectList';

export const SelectListContainer = styled.div`
  position: relative;

  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  border-radius: 8px;

  transition: all 0.2s ease-in-out;

  &:hover {
    gap: 0;
    background: #fff;
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
    background: #fff;
    a {
      width: 11rem;
      height: 3rem;
      padding: 0 0.8rem;
      border-radius: 0.8rem;
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

  background-color: ${({ isSelected, theme }) =>
    isSelected ? theme.color.light.primary_100 : theme.color.light.secondary_50};
  border-radius: 50%;

  transition: all 0.2s ease-in-out;

  &:hover {
    background: ${({ isSelected }) => isSelected || theme.color.light.primary_50};
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
