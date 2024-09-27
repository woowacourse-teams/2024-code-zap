import { useCategoryListQuery } from '@/queries/categories';
import type { Category } from '@/types';

import { useDropdown } from '../';

export const useCategory = (initCategory?: Category) => {
  const { data } = useCategoryListQuery();
  const options = data?.categories || [];

  if (!initCategory) {
    initCategory = { id: options[0]?.id, name: '카테고리' };
  }

  const { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef } = useDropdown<Category>(initCategory);

  const getOptionLabel = (category: Category) => category.name;

  return { options, isOpen, toggleDropdown, currentValue, handleCurrentValue, getOptionLabel, dropdownRef };
};
