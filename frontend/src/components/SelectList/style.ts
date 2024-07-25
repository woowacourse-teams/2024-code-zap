import styled from '@emotion/styled';

import type { OptionProps } from './SelectList';

export const SelectListOption = styled.a<OptionProps>`
  display: flex;
  align-items: center;

  width: 24rem;
  padding: 1rem 1.6rem;

  text-decoration: none;

  background-color: ${({ isSelected }) => isSelected && '#FFEBBB'};
  border-radius: 8px;
`;
