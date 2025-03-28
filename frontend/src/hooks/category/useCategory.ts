import { useTranslation } from 'react-i18next';

import { useDropdown } from '@/hooks';
import {
  useCategoryListQuery,
  useCategoryUploadMutation,
} from '@/queries/categories';
import type { Category } from '@/types';

interface Props {
  memberId: number;
  initCategory?: Category;
}

export const useCategory = ({ memberId, initCategory }: Props) => {
  const { t } = useTranslation();
  const { data, isFetching } = useCategoryListQuery({ memberId });
  const options = data?.categories || [];

  if (!initCategory) {
    initCategory = {
      id: options[0]?.id,
      name: t('Category:defaultCategories.noCategory'),
      ordinal: 0,
    };
  }

  const {
    isOpen,
    toggleDropdown,
    currentValue,
    handleCurrentValue,
    dropdownRef,
  } = useDropdown<Category>(initCategory);
  const { mutateAsync: postCategory, isPending } =
    useCategoryUploadMutation(handleCurrentValue);

  const getOptionLabel = (category: Category) => category.name;

  const getExistingCategory = (value: string) =>
    options.find((category) => getOptionLabel(category) === value);

  const createNewCategory = async (categoryName: string) => {
    const existingCategory = getExistingCategory(categoryName);

    if (existingCategory) {
      handleCurrentValue(existingCategory);

      return;
    }

    const newCategory = { name: categoryName, ordinal: options.length };

    await postCategory(newCategory);
  };

  return {
    options,
    isOpen,
    toggleDropdown,
    currentValue,
    handleCurrentValue,
    getOptionLabel,
    createNewCategory,
    dropdownRef,
    isPending,
    isCategoryQueryFetching: isFetching,
  };
};
