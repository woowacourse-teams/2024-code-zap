import { Category } from '@/types/category';
import { useDropdown } from '../utils/useDropdown';
import { useCategoryQuery } from './query/useCategoryQuery';

const INIT_CATEGORY = { id: 1, name: '카테고리 선택' };

export const useCategory = (initCategory: Category = INIT_CATEGORY) => {
  const { data } = useCategoryQuery();
  const options = data?.categories || [];
  const { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef } = useDropdown<Category>(initCategory);

  const getOptionLabel = (category: Category) => category.name;

  return { options, isOpen, toggleDropdown, currentValue, handleCurrentValue, getOptionLabel, dropdownRef };
};
