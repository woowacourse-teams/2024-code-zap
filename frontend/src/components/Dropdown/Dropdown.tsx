import { chevron } from '@/assets/images';
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
  <S.DropdownContainer ref={dropdownRef}>
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
      <S.OptionList>
        {options?.map((option, idx) => (
          <S.Option key={idx} onClick={() => handleCurrentValue(option)}>
            {getOptionLabel(option)}
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
    {getOptionLabel(currentValue)}
    <img
      src={chevron}
      width={24}
      height={24}
      alt=''
      css={{
        transition: 'transform 0.3s ease',
        transform: isOpen ? 'rotate(180deg)' : 'rotate(0deg)',
      }}
    />
  </S.SelectedButton>
);
