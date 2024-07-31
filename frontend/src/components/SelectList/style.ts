import styled from '@emotion/styled';

import type { OptionProps } from './SelectList';

export const SelectListContainer = styled.div`
  position: relative;

  display: flex;
  flex-direction: column;
  gap: 0.6rem;

  border-radius: 0.8rem;

  transition: all 0.2s ease-in-out;

  &:hover {
    gap: 0;
    background: #fff;
  }

  &:hover a {
    width: 24rem;
    height: 3.2rem;
    padding: 0 0.8rem;
    border-radius: 0.8rem;
  }

  &:hover .select-list-text {
    margin-left: 1rem;
    opacity: 1;
    transition: all 0.3s ease-in-out;
  }

  @media (min-width: 1360px) {
    gap: 0;
    background: #fff;
    a {
      width: 24rem;
      height: 3.2rem;
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

  background-color: ${({ isSelected, theme }) => (isSelected ? theme.color.dark.primary_100 : theme.color.dark.white)};
  border-radius: 50%;

  transition: all 0.2s ease-in-out;

  &:hover {
    background: ${({ isSelected }) => isSelected || '#eee'};
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
