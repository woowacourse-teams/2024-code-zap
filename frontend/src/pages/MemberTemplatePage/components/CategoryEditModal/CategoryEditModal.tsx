import { useState } from 'react';

import { Text, Modal, Flex, Button } from '@/components';
import { useCategoryNameValidation } from '@/hooks/category';
import { useCategoryEditMutation } from '@/queries/categories';
import { validateCategoryName } from '@/service/validates';
import type { Category } from '@/types';

import CategoryItems from './CategoryItems';
import * as S from './CategoryEditModal.style';

interface CategoryEditModalProps {
  isOpen: boolean;
  toggleModal: () => void;
  categories: Category[];
  handleCancelEdit: () => void;
  onDeleteCategory: (deletedIds: number[]) => void;
}

const CategoryEditModal = ({
  isOpen,
  toggleModal,
  categories,
  handleCancelEdit,
  onDeleteCategory,
}: CategoryEditModalProps) => {
  const [editedCategories, setEditedCategories] = useState<Category[]>([]);
  const [newCategories, setNewCategories] = useState<Category[]>([]);
  const [deleteCategoryIds, setDeleteCategoryIds] = useState<number[]>([]);
  const [editingCategoryId, setEditingCategoryId] = useState<number | null>(null);

  const { mutateAsync: editCategory } = useCategoryEditMutation();

  const { invalidIds, isValid } = useCategoryNameValidation(categories, newCategories, editedCategories);

  const resetState = () => {
    setEditedCategories([]);
    setDeleteCategoryIds([]);
    setNewCategories([]);
    setEditingCategoryId(null);
  };

  const isNewCategory = (id: number) => newCategories.some((category) => category.id === id);

  const handleNameInputChange = (id: number, name: string) => {
    const errorMessage = validateCategoryName(name);

    if (errorMessage && name.length > 0) {
      return;
    }

    if (isNewCategory(id)) {
      setNewCategories((prev) => prev.map((category) => (category.id === id ? { ...category, name } : category)));

      return;
    }

    //TODO: targetCategory 네이밍 바꾸기
    const targetCategory = editedCategories.find((category) => category.id === id);

    if (targetCategory) {
      setEditedCategories((prev) => prev.map((category) => (category.id === id ? { ...category, name } : category)));

      return;
    }

    const ordinal = categories.find((category) => category.id === id)?.ordinal as number;

    setEditedCategories((prev) => [...prev, { id, name, ordinal }]);
  };

  const handleOrdinalChange = (categories: Category[]) => {
    const updatedCategories: Category[] = [];
    const updatedNewCategories: Category[] = [];

    categories.forEach((category) => {
      if (isNewCategory(category.id)) {
        updatedNewCategories.push(category);
      } else {
        updatedCategories.push({
          ...category,
          name: editedCategories.find((editedCategory) => editedCategory.id === category.id)?.name ?? category.name,
        });
      }
    });

    setNewCategories(updatedNewCategories);
    setEditedCategories(updatedCategories);
  };

  const handleDeleteClick = (id: number) => {
    if (isNewCategory(id)) {
      setNewCategories((prev) => prev.filter((category) => category.id !== id));
    } else {
      setDeleteCategoryIds((prev) => [...prev, id]);
    }
  };

  const handleRestoreClick = (id: number) => {
    setDeleteCategoryIds((prev) => prev.filter((categoryId) => categoryId !== id));
  };

  const handleEditClick = (id: number) => {
    setEditingCategoryId(id);
  };

  const handleNameInputBlur = (id: number) => {
    const trimmedName = isNewCategory(id)
      ? newCategories.find((category) => category.id === id)?.name.trim()
      : editedCategories.find((category) => category.id === id)?.name.trim();

    if (trimmedName !== undefined) {
      handleNameInputChange(id, trimmedName);
    }

    setEditingCategoryId(null);
  };

  const handleAddCategory = () => {
    const id =
      categories.length > 0
        ? categories[categories.length - 1].id + newCategories.length + 1
        : newCategories.length + 1;

    const ordinal = categories.length + 1 + newCategories.length;

    setNewCategories((prev) => [...prev, { id, name: '', ordinal }]);
    setEditingCategoryId(id);
  };

  const handleSaveChanges = async () => {
    if (!isValid) {
      return;
    }

    const body = {
      createCategories: newCategories.map(({ name, ordinal }) => ({ name, ordinal })),
      updateCategories: editedCategories,
      deleteCategoryIds,
    };

    await editCategory(body);

    if (deleteCategoryIds.length > 0) {
      onDeleteCategory(deleteCategoryIds);
    }

    resetState();
    toggleModal();
  };

  const handleCancelEditWithReset = () => {
    resetState();
    handleCancelEdit();
  };

  return (
    <Modal isOpen={isOpen} toggleModal={handleCancelEditWithReset} size='small'>
      <Modal.Header>{'카테고리 편집'}</Modal.Header>
      <Modal.Body>
        <S.EditCategoryItemList>
          <CategoryItems
            categories={categories}
            newCategories={newCategories}
            handleDrag={handleOrdinalChange}
            editedCategories={editedCategories}
            categoriesToDelete={deleteCategoryIds}
            editingCategoryId={editingCategoryId}
            invalidIds={invalidIds}
            isNewCategory={isNewCategory}
            onEditClick={handleEditClick}
            onDeleteClick={handleDeleteClick}
            onRestoreClick={handleRestoreClick}
            onNameInputChange={handleNameInputChange}
            onNameInputBlur={handleNameInputBlur}
          />
          <S.EditCategoryItem isButton={true} disabled={!isValid}>
            <Button fullWidth variant='text' hoverStyle='none' onClick={handleAddCategory} disabled={!isValid}>
              {'+ 카테고리 추가'}
            </Button>
          </S.EditCategoryItem>
        </S.EditCategoryItemList>
      </Modal.Body>
      <Modal.Footer>
        <Flex direction='column' gap='0.75rem' width='100%' style={{ alignSelf: 'flex-end' }}>
          <Flex height='1em'>
            {invalidIds.length > 0 && (
              <Text.Small color={'red'}>{'유효하지 않은 카테고리 이름이 있습니다.'}</Text.Small>
            )}
          </Flex>
          <Flex justify='flex-end' gap='1rem'>
            <Button variant='outlined' onClick={handleCancelEditWithReset}>
              {'취소'}
            </Button>
            <Button onClick={handleSaveChanges} disabled={!isValid}>
              {'저장'}
            </Button>
          </Flex>
        </Flex>
      </Modal.Footer>
    </Modal>
  );
};

export default CategoryEditModal;
