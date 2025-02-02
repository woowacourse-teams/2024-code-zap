import { Dropdown, CategoryGuide, NewCategoryInput } from '@/components';
import { useInputWithValidate } from '@/hooks';
import { validateCategoryName } from '@/service/validates';
import { Category } from '@/types';

import * as S from './CategoryDropdown.style';

interface Props {
  options: Category[];
  isOpen: boolean;
  toggleDropdown: () => void;
  currentValue: Category;
  handleCurrentValue: (newValue: Category) => void;
  getOptionLabel: (category: Category) => string;
  createNewCategory: (categoryName: string) => Promise<void>;
  dropdownRef: React.MutableRefObject<HTMLDivElement | null>;
  isPending: boolean;
}

const CategoryDropdown = ({
  options,
  isOpen,
  toggleDropdown,
  currentValue,
  handleCurrentValue,
  getOptionLabel,
  createNewCategory,
  dropdownRef,
  isPending,
}: Props) => {
  const {
    value: categoryInputValue,
    errorMessage: categoryInputErrorMessage,
    handleChange: handleCategoryInputChange,
  } = useInputWithValidate('', validateCategoryName);

  const handleNewCategory = async (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (!(e.target instanceof HTMLInputElement) || e.key !== 'Enter' || e.nativeEvent.isComposing === true) {
      return;
    }

    if (categoryInputErrorMessage !== '') {
      return;
    }

    const inputValue = e.target.value;

    if (inputValue === '') {
      return;
    }

    await createNewCategory(inputValue);

    e.target.value = '';
  };

  return (
    <S.CategoryDropdownContainer>
      <CategoryGuide isOpen={isOpen} categoryErrorMessage={categoryInputErrorMessage} />
      <Dropdown
        options={options}
        isOpen={isOpen}
        toggleDropdown={toggleDropdown}
        currentValue={currentValue}
        handleCurrentValue={handleCurrentValue}
        getOptionLabel={getOptionLabel}
        dropdownRef={dropdownRef}
        replaceChildrenWhenIsOpen={
          <NewCategoryInput
            value={categoryInputValue}
            onChange={handleCategoryInputChange}
            onEnterDown={handleNewCategory}
            isPending={isPending}
          />
        }
      />
    </S.CategoryDropdownContainer>
  );
};

export default CategoryDropdown;
