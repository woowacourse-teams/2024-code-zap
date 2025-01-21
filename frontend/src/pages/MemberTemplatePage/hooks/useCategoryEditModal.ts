import { useEffect, useState } from 'react';

import { useCategoryNameValidation } from '@/hooks/category';
import { useCategoryEditMutation } from '@/queries/categories';
import { validateCategoryName } from '@/service/validates';
import { Category } from '@/types';

interface Props {
  categoryList: Category[];
  toggleModal: () => void;
  onDeleteCategory: (deletedIds: number[]) => void;
}

export const useCategoryEditModal = ({ categoryList, toggleModal, onDeleteCategory }: Props) => {
  const [editedCategoryList, setEditedCategoryList] = useState<Category[]>([...categoryList]);
  const [deleteCategoryIds, setDeleteCategoryIds] = useState<number[]>([]);
  const [editingCategoryId, setEditingCategoryId] = useState<number | null>(null);

  const { mutateAsync: editCategory } = useCategoryEditMutation();

  const { invalidIds, isValid } = useCategoryNameValidation(categoryList, editedCategoryList);

  useEffect(() => {
    if (!isEqualCategoryList(categoryList, editedCategoryList)) {
      setEditedCategoryList([...categoryList]);
    }
  }, [categoryList]);

  const isEqualCategoryList = (arr1: Category[], arr2: Category[]): boolean => {
    if (arr1.length !== arr2.length) {
      return false;
    }

    return arr1.every((category, index) => {
      const category2 = arr2[index];

      return category.id === category2.id && category.name === category2.name && category.ordinal === category2.ordinal;
    });
  };

  const isNewCategory = (id: number) => categoryList.every((category) => category.id !== id);

  const resetState = () => {
    setEditedCategoryList([...categoryList]);
    setDeleteCategoryIds([]);
    setEditingCategoryId(null);
  };

  const handleNameInputChange = (id: number, name: string) => {
    const errorMessage = validateCategoryName(name);

    if (errorMessage && name.length > 0) {
      return;
    }

    setEditedCategoryList((prev) => prev.map((category) => (category.id === id ? { ...category, name } : category)));
  };

  const handleOrdinalChange = (categoryList: Category[]) => {
    const updatedCategoryList = categoryList.map((category, index) => ({
      ...category,
      ordinal: index + 1,
    }));

    setEditedCategoryList(updatedCategoryList);
  };

  const handleDeleteClick = (id: number) => {
    if (isNewCategory(id)) {
      setEditedCategoryList((prev) => prev.filter((category) => category.id !== id));

      const updatedCategoryList = [...editedCategoryList.filter((category) => category.id !== id)].sort(
        (a, b) => a.ordinal - b.ordinal,
      );

      handleOrdinalChange(updatedCategoryList);

      return;
    }

    setDeleteCategoryIds((prev) => [...prev, id]);

    const updatedCategoryList = [...editedCategoryList].sort((a, b) => a.ordinal - b.ordinal);

    handleOrdinalChange(updatedCategoryList);
  };

  const handleRestoreClick = (id: number) => {
    setDeleteCategoryIds((prev) => prev.filter((categoryId) => categoryId !== id));
  };

  const handleEditClick = (id: number) => {
    setEditingCategoryId(id);
  };

  const handleNameInputBlur = (id: number) => {
    const trimmedName = editedCategoryList.find((category) => category.id === id)?.name.trim();

    if (trimmedName !== undefined) {
      handleNameInputChange(id, trimmedName);
    }

    setEditingCategoryId(null);
  };

  const handleAddCategory = () => {
    const id = Date.now();

    const ordinal = editedCategoryList.length + 1;

    setEditedCategoryList((prev) => [...prev, { id, name: '', ordinal }]);
    setEditingCategoryId(id);
  };

  const handleSaveChanges = async () => {
    if (!isValid) {
      return;
    }

    const body = getCategoryEditRequestBody();

    await editCategory(body);

    if (deleteCategoryIds.length > 0) {
      onDeleteCategory(deleteCategoryIds);
    }

    resetState();
    toggleModal();
  };

  const getCategoryEditRequestBody = () => {
    const filteredCategoryList = editedCategoryList
      .filter(({ id }) => !deleteCategoryIds.includes(id))
      .map((category, idx) => ({ ...category, ordinal: idx + 1 }));

    const body = {
      createCategories: filteredCategoryList
        .filter(({ id }) => isNewCategory(id))
        .map(({ name, ordinal }) => ({ name, ordinal })),
      updateCategories: filteredCategoryList.filter(({ id }) => !isNewCategory(id)),
      deleteCategoryIds,
    };

    return body;
  };

  const handleCancelEditWithReset = () => {
    resetState();
    toggleModal();
  };

  return {
    editedCategoryList,
    deleteCategoryIds,
    editingCategoryId,
    invalidIds,
    isValid,
    isNewCategory,
    handleNameInputChange,
    handleOrdinalChange,
    handleDeleteClick,
    handleRestoreClick,
    handleEditClick,
    handleNameInputBlur,
    handleAddCategory,
    handleSaveChanges,
    handleCancelEditWithReset,
  };
};
