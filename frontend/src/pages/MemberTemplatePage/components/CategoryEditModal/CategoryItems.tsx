import { useRef } from 'react';

import { theme } from '@/style/theme';
import { Category } from '@/types';

import ExistingCategoryItem from './ExistingCategoryItem';
import NewCategoryItem from './NewCategoryItem';
import * as S from './CategoryEditModal.style';

interface CategoryItemsProps {
  editedCategoryList: Category[];
  deleteCategoryIds: number[];
  editingCategoryId: number | null;
  invalidIds: number[];
  isNewCategory: (id: number) => boolean;
  handleOrdinalChange: (categoryList: Category[]) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
  onRestoreClick: (id: number) => void;
  onNameInputChange: (id: number, name: string) => void;
  onNameInputBlur: (id: number) => void;
}

const CategoryItems = ({
  editedCategoryList,
  deleteCategoryIds,
  editingCategoryId,
  invalidIds,
  isNewCategory,
  handleOrdinalChange,
  onEditClick,
  onDeleteClick,
  onRestoreClick,
  onNameInputChange,
  onNameInputBlur,
}: CategoryItemsProps) => {
  const orderedCategoryList = [...editedCategoryList].sort(
    (a, b) => a.ordinal - b.ordinal,
  );

  const dragItem = useRef<number | null>(null);
  const dragOverItem = useRef<number | null>(null);

  const handleDragStart = (
    e: React.DragEvent<HTMLDivElement>,
    position: number,
  ) => {
    dragItem.current = position;
    e.currentTarget.style.opacity = '0.5';
  };

  const handleDragEnter = (
    e: React.DragEvent<HTMLDivElement>,
    position: number,
  ) => {
    dragOverItem.current = position;
    e.currentTarget.style.backgroundColor = theme.color.dark.white;
  };

  const handleDragEnd = (e: React.DragEvent<HTMLDivElement>) => {
    if (dragItem.current === null || dragOverItem.current === null) {
      return;
    }

    e.currentTarget.style.opacity = '1';
    e.currentTarget.style.backgroundColor = '';

    const reorderedCategoryList = getReorderedCategoryList(
      orderedCategoryList,
      dragItem.current,
      dragOverItem.current,
    );

    handleOrdinalChange(reorderedCategoryList);

    dragItem.current = null;
    dragOverItem.current = null;
  };

  const getReorderedCategoryList = (
    categoryList: Category[],
    startIndex: number,
    endIndex: number,
  ) => {
    const copyListItems = [...categoryList];
    const dragItem = copyListItems[startIndex];

    copyListItems.splice(startIndex, 1);
    copyListItems.splice(endIndex, 0, dragItem);

    return copyListItems;
  };

  const handleDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
    e.currentTarget.style.backgroundColor = '';
  };

  return (
    <>
      {orderedCategoryList.map(({ id, name }, index) => (
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
              name={
                editedCategoryList.find((category) => category.id === id)
                  ?.name ?? name
              }
              isEditing={editingCategoryId === id}
              isDeleted={deleteCategoryIds.includes(id)}
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
