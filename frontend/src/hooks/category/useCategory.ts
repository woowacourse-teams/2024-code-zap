import { useCategoryListQuery } from '@/queries/category';
import type { Category } from '@/types';
import { useDropdown } from '../utils/useDropdown';

export const useCategory = (initCategory?: Category) => {
  const { data } = useCategoryListQuery();
  const options = data?.categories || [];

  if (!initCategory) {
    initCategory = { id: options[0].id, name: '카테고리 선택' };
  }

  const { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef } = useDropdown<Category>(initCategory);

  const getOptionLabel = (category: Category) => category.name;

  return { options, isOpen, toggleDropdown, currentValue, handleCurrentValue, getOptionLabel, dropdownRef };
};
