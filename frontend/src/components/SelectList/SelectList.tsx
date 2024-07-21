import React, { ReactNode } from 'react';
import { Text } from '../Text';
import * as S from './style';

interface Props {
  children?: ReactNode;
}

export interface OptionProps {
  children?: string;
  isSelected: boolean;
  onClick?: (event: React.MouseEvent<HTMLAnchorElement>) => void;
}

const SelectListBase = ({ children }: Props) => <S.SelectListContainer>{children}</S.SelectListContainer>;

const SelectListOption = ({ children, isSelected, onClick }: OptionProps) => (
  <S.SelectListOption href={`#${children}`} onClick={onClick} isSelected={isSelected}>
    <Text.Body color={isSelected ? 'black' : undefined}>{children}</Text.Body>
  </S.SelectListOption>
);

const SelectList = Object.assign(SelectListBase, {
  Option: SelectListOption,
});

export default SelectList;
