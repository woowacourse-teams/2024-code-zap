import { useCategoryListQuery } from '@/queries/category';
import type { Category } from '@/types';
import { useDropdown } from '../utils/useDropdown';

const INIT_CATEGORY = { id: 1, name: '카테고리 선택' };

export const useCategory = (initCategory: Category = INIT_CATEGORY) => {
  const { data } = useCategoryListQuery();
  const options = data?.categories || [];
  const { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef } = useDropdown<Category>(initCategory);

  const getOptionLabel = (category: Category) => category.name;

  return { options, isOpen, toggleDropdown, currentValue, handleCurrentValue, getOptionLabel, dropdownRef };
};
