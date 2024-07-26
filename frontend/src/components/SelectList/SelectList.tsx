import { PropsWithChildren } from 'react';

import { Text } from '@/components';
import * as S from './style';

export interface OptionProps {
  isSelected: boolean;
  onClick: (event: React.MouseEvent<HTMLAnchorElement>) => void;
}

const SelectListBase = ({ children }: PropsWithChildren) => <S.SelectListContainer>{children}</S.SelectListContainer>;

const SelectListOption = ({ children, isSelected, onClick }: PropsWithChildren<OptionProps>) => (
  <S.SelectListOption href={`#${children}`} onClick={onClick} isSelected={isSelected}>
    <S.SelectListText className='select-list-text'>
      <Text.Body color={isSelected ? 'black' : '#393E46'}>{children}</Text.Body>
    </S.SelectListText>
  </S.SelectListOption>
);

const SelectList = Object.assign(SelectListBase, {
  Option: SelectListOption,
});

export default SelectList;
