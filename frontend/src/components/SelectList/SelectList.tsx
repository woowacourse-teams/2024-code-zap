import { PropsWithChildren } from 'react';
import { Text } from '..';
import * as S from './style';

export interface OptionProps {
  isSelected: boolean;
  onClick: (event: React.MouseEvent<HTMLAnchorElement>) => void;
}

const SelectListBase = ({ children }: PropsWithChildren) => <S.SelectListContainer>{children}</S.SelectListContainer>;

const SelectListOption = ({ children, isSelected, onClick }: PropsWithChildren<OptionProps>) => (
  <S.SelectListOption href={`#${children}`} onClick={onClick} isSelected={isSelected}>
    <Text.Body color={isSelected ? 'black' : 'white'}>{children}</Text.Body>
  </S.SelectListOption>
);

const SelectList = Object.assign(SelectListBase, {
  Option: SelectListOption,
});

export default SelectList;
