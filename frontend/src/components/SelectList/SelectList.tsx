import { useTheme } from '@emotion/react';
import { PropsWithChildren } from 'react';

import { Text } from '@/components';
import * as S from './SelectList.style';

export interface OptionProps {
  isSelected: boolean;
  onClick: (event: React.MouseEvent<HTMLAnchorElement>) => void;
}

const SelectListBase = ({ children }: PropsWithChildren) => <S.SelectListContainer>{children}</S.SelectListContainer>;

const SelectListOption = ({ children, isSelected, onClick }: PropsWithChildren<OptionProps>) => {
  const theme = useTheme();

  return (
    <S.SelectListOption href={`#${children}`} onClick={onClick} isSelected={isSelected}>
      <S.SelectListText className='select-list-text'>
        <Text.Medium color={isSelected ? theme.color.light.white : theme.color.light.secondary_600}>
          {children}
        </Text.Medium>
      </S.SelectListText>
    </S.SelectListOption>
  );
};

const SelectList = Object.assign(SelectListBase, {
  Option: SelectListOption,
});

export default SelectList;
