import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';

import type { Category } from '@/types';

const INVALID_KEYS = [
  'defaultCategories.viewAll',
  'defaultCategories.noCategory',
  '',
];

export const useCategoryNameValidation = (
  categoryList: Category[],
  editedCategoryList: Category[],
) => {
  const { t } = useTranslation();
  const [invalidIds, setInvalidIds] = useState<number[]>([]);
  const INVALID_NAMES = INVALID_KEYS.map((key) =>
    key ? t(`Category:${key}`) : '',
  );

  useEffect(() => {
    const allNames = new Map<string, number[]>();
    const invalidNames = new Set<number>();

    const addNameToMap = (id: number, name: string) => {
      if (!allNames.has(name)) {
        allNames.set(name, []);
      }

      allNames.get(name)!.push(id);
    };

    categoryList.forEach(({ id, name }) => {
      if (INVALID_NAMES.includes(name)) {
        invalidNames.add(id);
      } else {
        addNameToMap(id, name);
      }
    });

    editedCategoryList.forEach(({ id, name }) => {
      const originalName = categoryList.find(
        (category) => category.id === id,
      )?.name;

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
  }, [categoryList, editedCategoryList, INVALID_NAMES]);

  return {
    invalidIds,
    isValid: invalidIds.length === 0,
  };
};
