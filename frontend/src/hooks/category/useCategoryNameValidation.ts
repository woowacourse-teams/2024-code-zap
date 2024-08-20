import { useState, useEffect } from 'react';

import type { Category } from '@/types';

const INVALID_NAMES = ['전체보기', '카테고리 없음', ''];

export const useCategoryNameValidation = (
  categories: Category[],
  newCategories: { id: number; name: string }[],
  editedCategories: Record<number, string>,
) => {
  const [invalidIds, setInvalidIds] = useState<number[]>([]);

  useEffect(() => {
    const allNames = new Set<string>();
    const invalidNames = new Set<number>();

    categories.forEach(({ id, name }) => {
      if (INVALID_NAMES.includes(name) || allNames.has(name)) {
        invalidNames.add(id);
      } else {
        allNames.add(name);
      }
    });

    newCategories.forEach(({ id, name }) => {
      if (INVALID_NAMES.includes(name) || allNames.has(name)) {
        invalidNames.add(id);
      } else {
        allNames.add(name);
      }
    });

    Object.entries(editedCategories).forEach(([id, name]) => {
      const originalName = categories.find((category) => category.id === Number(id))?.name;

      if (INVALID_NAMES.includes(name) || (name !== originalName && allNames.has(name))) {
        invalidNames.add(Number(id));
      } else if (name !== originalName) {
        allNames.add(name);
      }
    });

    setInvalidIds(Array.from(invalidNames));
  }, [categories, newCategories, editedCategories]);

  return {
    invalidIds,
    isValid: invalidIds.length === 0,
  };
};
