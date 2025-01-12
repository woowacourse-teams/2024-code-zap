import { useEffect, useRef, useState } from 'react';

import { Category } from '@/types';

import ExistingCategoryItem from './ExistingCategoryItem';
import NewCategoryItem from './NewCategoryItem';
import * as S from './CategoryEditModal.style';

interface CategoryItemsProps {
  categories: Category[];
  newCategories: Category[];
  editedCategories: Category[];
  categoriesToDelete: number[];
  editingCategoryId: number | null;
  invalidIds: number[];
  isNewCategory: (id: number) => boolean;
  handleDrag: (categories: Category[]) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
  onRestoreClick: (id: number) => void;
  onNameInputChange: (id: number, name: string) => void;
  onNameInputBlur: (id: number) => void;
}

const CategoryItems = ({
  categories,
  newCategories,
  editedCategories,
  categoriesToDelete,
  editingCategoryId,
  invalidIds,
  isNewCategory,
  handleDrag,
  onEditClick,
  onDeleteClick,
  onRestoreClick,
  onNameInputChange,
  onNameInputBlur,
}: CategoryItemsProps) => {
  const categoriesMap = new Map();

  [...categories, ...editedCategories, ...newCategories].forEach((category) => {
    categoriesMap.set(category.id, category);
  });

  const initOrderedCategoriesArray = Array.from(categoriesMap.values()).sort((a, b) => a.ordinal - b.ordinal);

  const [orderedCategories, setOrderedCategories] = useState(initOrderedCategoriesArray);

  useEffect(() => {
    const categoriesMap = new Map();

    [...categories, ...editedCategories, ...newCategories].forEach((category) => {
      categoriesMap.set(category.id, category);
    });

    const orderedCategoriesArray = Array.from(categoriesMap.values()).sort((a, b) => a.ordinal - b.ordinal);

    setOrderedCategories(orderedCategoriesArray);
  }, [newCategories, editedCategories, categories]);

  const dragItem = useRef<number | null>(null);
  const dragOverItem = useRef<number | null>(null);

  const handleDragStart = (e: React.DragEvent<HTMLDivElement>, position: number) => {
    dragItem.current = position;
    e.currentTarget.style.opacity = '0.5';
  };

  const handleDragEnter = (e: React.DragEvent<HTMLDivElement>, position: number) => {
    dragOverItem.current = position;
    e.currentTarget.style.backgroundColor = '#f5f5f5';
  };

  const handleDragEnd = (e: React.DragEvent<HTMLDivElement>) => {
    if (dragItem.current === null || dragOverItem.current === null) {
      return;
    }

    e.currentTarget.style.opacity = '1';
    e.currentTarget.style.backgroundColor = '';

    const copyListItems = [...orderedCategories];
    const dragItemContent = copyListItems[dragItem.current];

    copyListItems.splice(dragItem.current, 1);
    copyListItems.splice(dragOverItem.current, 0, dragItemContent);

    const updatedItems = copyListItems.map((item, index) => ({
      ...item,
      ordinal: index + 1,
    }));

    dragItem.current = null;
    dragOverItem.current = null;
    handleDrag(updatedItems);
    setOrderedCategories(updatedItems);
  };

  const handleDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
    e.currentTarget.style.backgroundColor = '';
  };

  return (
    <>
      {orderedCategories.map(({ id, name }, index) => (
        <S.EditCategoryItem
          key={id}
          hasError={invalidIds.includes(id)}
          draggable
          onDragStart={(e) => handleDragStart(e, index)}
          onDragEnter={(e) => handleDragEnter(e, index)}
          onDragEnd={handleDragEnd}
          onDragLeave={handleDragLeave}
          onDragOver={(e) => e.preventDefault()}
        >
          {isNewCategory(id) ? (
            <NewCategoryItem
              id={id}
              name={name}
              isEditing={editingCategoryId === id}
              onChange={(e) => onNameInputChange(id, e.target.value)}
              onBlur={() => onNameInputBlur(id)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  onNameInputBlur(id);
                }
              }}
              onEditClick={() => onEditClick(id)}
              onDeleteClick={() => onDeleteClick(id)}
            />
          ) : (
            <ExistingCategoryItem
              id={id}
              name={editedCategories.find((category) => category.id === id)?.name ?? name}
              isEditing={editingCategoryId === id}
              isDeleted={categoriesToDelete.includes(id)}
              onChange={(e) => onNameInputChange(id, e.target.value)}
              onBlur={() => onNameInputBlur(id)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  onNameInputBlur(id);
                }
              }}
              onEditClick={() => onEditClick(id)}
              onDeleteClick={() => onDeleteClick(id)}
              onRestoreClick={() => onRestoreClick(id)}
            />
          )}
        </S.EditCategoryItem>
      ))}
    </>
  );
};

export default CategoryItems;
