import styled from '@emotion/styled';
import { OptionProps } from './SelectList';

type OptionStyleProps = Pick<OptionProps, 'isSelected'>;

export const SelectListContainer = styled.aside`
  position: fixed;
  display: flex;
  flex-direction: column;
`;

export const SelectListOption = styled.a<OptionStyleProps>`
  display: flex;
  align-items: center;

  width: 24rem;
  padding: 1rem 1.6rem;

  text-decoration: none;

  background-color: ${({ isSelected }) => isSelected || '#FFEBBB'};
  border-radius: 8px;
`;
