import { Flex } from '../Flex';
import { ReactNode } from 'react';
import { Text } from '../Text';

interface Props {
  children?: ReactNode;
}

interface SelectListOptionProps {
  children?: ReactNode;
  isSelected: boolean;
  onClick?: (event: React.MouseEvent<HTMLAnchorElement>) => void;
}

const SelectListContainer = ({ children }: Props) => {
  return (
    <Flex direction='column'>
      <aside css={{ position: 'fixed' }}>{children}</aside>
    </Flex>
  );
};

const SelectListOption = ({ children, isSelected, onClick }: SelectListOptionProps) => {
  return (
    <a href={`#${children}`} onClick={onClick}>
      <div
        css={{
          backgroundColor: isSelected ? '#FFEBBB' : undefined,
          padding: '1rem 1.6rem',
          borderRadius: '8px',
          width: '24rem',
        }}
      >
        <Text.Body color={isSelected ? 'black' : undefined}>{children}</Text.Body>
      </div>
    </a>
  );
};

const SelectList = Object.assign(SelectListContainer, {
  Option: SelectListOption,
});

export default SelectList;
