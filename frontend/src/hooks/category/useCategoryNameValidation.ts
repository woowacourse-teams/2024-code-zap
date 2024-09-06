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
    const allNames = new Map<string, number[]>();
    const invalidIdsSet = new Set<number>();

    const checkAndAddName = (id: number, name: string) => {
      if (INVALID_NAMES.includes(name)) {
        invalidIdsSet.add(id);
      } else {
        if (!allNames.has(name)) {
          allNames.set(name, []);
        }

        allNames.get(name)!.push(id);
      }
    };

    categories.forEach(({ id, name }) => checkAndAddName(id, editedCategories[id] ?? name));
    newCategories.forEach(({ id, name }) => checkAndAddName(id, name));

    allNames.forEach((ids) => {
      if (ids.length > 1) {
        ids.forEach((id) => invalidIdsSet.add(id));
      }
    });

    setInvalidIds(Array.from(invalidIdsSet));
  }, [categories, newCategories, editedCategories]);

  return {
    invalidIds,
    isValid: invalidIds.length === 0,
  };
};
