import { ChevronIcon } from '@/assets/images';

import { theme } from '../../style/theme';
import Text from '../Text/Text';
import * as S from './Dropdown.style';

interface Props<T> {
  dropdownRef: React.MutableRefObject<HTMLDivElement | null>;
  isOpen: boolean;
  toggleDropdown: () => void;
  options: T[];
  getOptionLabel: (option: T) => string;
  currentValue: T;
  handleCurrentValue: (value: T) => void;
  replaceChildrenWhenIsOpen?: React.ReactNode;
}

const Dropdown = <T,>({
  dropdownRef,
  isOpen,
  toggleDropdown,
  options,
  getOptionLabel,
  currentValue,
  handleCurrentValue,
  replaceChildrenWhenIsOpen,
}: Props<T>) => (
  <S.DropdownContainer ref={dropdownRef} data-testid='dropdown-container'>
    <S.Wrapper>
      {isOpen ? (
        replaceChildrenWhenIsOpen || (
          <SelectedButton
            isOpen={isOpen}
            toggleDropdown={toggleDropdown}
            getOptionLabel={getOptionLabel}
            currentValue={currentValue}
          />
        )
      ) : (
        <SelectedButton
          isOpen={isOpen}
          toggleDropdown={toggleDropdown}
          getOptionLabel={getOptionLabel}
          currentValue={currentValue}
        />
      )}
    </S.Wrapper>
    {isOpen && (
      <S.OptionList data-testid='dropdown-option-list'>
        {options?.map((option, idx) => (
          <S.Option key={idx} onClick={() => handleCurrentValue(option)}>
            <Text.Small color={theme.color.light.black}>{getOptionLabel(option)}</Text.Small>
          </S.Option>
        ))}
      </S.OptionList>
    )}
  </S.DropdownContainer>
);

export default Dropdown;

type SelectedButtonProps<T> = Pick<Props<T>, 'toggleDropdown' | 'getOptionLabel' | 'currentValue' | 'isOpen'>;

const SelectedButton = <T,>({ toggleDropdown, getOptionLabel, currentValue, isOpen }: SelectedButtonProps<T>) => (
  <S.SelectedButton onClick={toggleDropdown}>
    <Text.Small color={theme.color.light.black}>{getOptionLabel(currentValue)}</Text.Small>
    <ChevronIcon
      width={16}
      height={16}
      aria-label='정렬기준 펼침'
      css={{ transition: 'transform 0.3s ease', transform: isOpen ? 'rotate(180deg)' : 'rotate(0deg)' }}
    />
  </S.SelectedButton>
);
