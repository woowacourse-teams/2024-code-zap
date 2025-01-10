import { useState, useEffect } from 'react';

import type { Category } from '@/types';

const INVALID_NAMES = ['전체보기', '카테고리 없음', ''];

export const useCategoryNameValidation = (
  categories: Category[],
  newCategories: Category[],
  editedCategories: Category[],
) => {
  const [invalidIds, setInvalidIds] = useState<number[]>([]);

  useEffect(() => {
    const allNames = new Map<string, number[]>();
    const invalidNames = new Set<number>();

    const addNameToMap = (id: number, name: string) => {
      if (!allNames.has(name)) {
        allNames.set(name, []);
      }

      allNames.get(name)!.push(id);
    };

    categories.forEach(({ id, name }) => {
      if (INVALID_NAMES.includes(name)) {
        invalidNames.add(id);
      } else {
        addNameToMap(id, name);
      }
    });

    newCategories.forEach(({ id, name }) => {
      if (INVALID_NAMES.includes(name)) {
        invalidNames.add(id);
      } else {
        addNameToMap(id, name);
      }
    });

    editedCategories.forEach(({ id, name }) => {
      const originalName = categories.find((category) => category.id === id)?.name;

      if (INVALID_NAMES.includes(name)) {
        invalidNames.add(id);
      } else if (name !== originalName) {
        addNameToMap(id, name);
      }
    });

    allNames.forEach((ids) => {
      if (ids.length > 1) {
        ids.forEach((id) => invalidNames.add(id));
      }
    });

    setInvalidIds(Array.from(invalidNames));
  }, [categories, newCategories, editedCategories]);

  return {
    invalidIds,
    isValid: invalidIds.length === 0,
  };
};
