import { useState } from 'react';

import { TrashcanIcon, SpinArrowIcon, PencilIcon } from '@/assets/images';
import { Heading, Text, Modal, Input, Flex, Button } from '@/components';
import { useCategoryNameValidation } from '@/hooks/category';
import { useCategoryDeleteMutation, useCategoryEditMutation, useCategoryUploadMutation } from '@/queries/category';
import type { Category, CustomError } from '@/types';
import { theme } from '../../style/theme';
import * as S from './CategoryEditModal.style';

interface CategoryEditModalProps {
  isOpen: boolean;
  toggleModal: () => void;
  categories: Category[];
  defaultCategory: Category;
  handleCancelEdit: () => void;
}

const CategoryEditModal = ({ isOpen, toggleModal, categories, handleCancelEdit }: CategoryEditModalProps) => {
  const [editedCategories, setEditedCategories] = useState<Record<number, string>>({});
  const [categoriesToDelete, setCategoriesToDelete] = useState<number[]>([]);
  const [newCategories, setNewCategories] = useState<{ id: number; name: string }[]>([]);
  const [editingCategoryId, setEditingCategoryId] = useState<number | null>(null);

  const { mutateAsync: editCategory } = useCategoryEditMutation();
  const { mutateAsync: deleteCategory } = useCategoryDeleteMutation(categories);
  const { mutateAsync: postCategory } = useCategoryUploadMutation();

  const { invalidIds, isValid } = useCategoryNameValidation(categories, newCategories, editedCategories);

  const resetState = () => {
    setEditedCategories({});
    setCategoriesToDelete([]);
    setNewCategories([]);
    setEditingCategoryId(null);
  };

  const isCategoryNew = (id: number) => newCategories.some((category) => category.id === id);

  const handleNameInputChange = (id: number, name: string) => {
    if (isCategoryNew(id)) {
      setNewCategories((prev) => prev.map((category) => (category.id === id ? { ...category, name } : category)));
    } else {
      setEditedCategories((prev) => ({ ...prev, [id]: name }));
    }
  };

  const handleDeleteClick = (id: number) => {
    if (isCategoryNew(id)) {
      setNewCategories((prev) => prev.filter((category) => category.id !== id));
    } else {
      setCategoriesToDelete((prev) => [...prev, id]);
    }
  };

  const handleRestoreClick = (id: number) => {
    setCategoriesToDelete((prev) => prev.filter((categoryId) => categoryId !== id));
  };

  const handleEditClick = (id: number) => {
    setEditingCategoryId(id);
  };

  const handleNameInputBlur = (id: number) => {
    const trimmedName = isCategoryNew(id)
      ? newCategories.find((category) => category.id === id)?.name.trim()
      : editedCategories[id]?.trim();

    if (trimmedName !== undefined) {
      handleNameInputChange(id, trimmedName);
    }

    setEditingCategoryId(null);
  };

  const handleAddCategory = () => {
    const newCategoryId = categories.length + newCategories.length;
    const newCategoryName = `카테고리 ${newCategoryId + 1}`;

    setNewCategories((prev) => [...prev, { id: newCategoryId, name: newCategoryName }]);
    setEditingCategoryId(newCategoryId);
  };

  const handleSaveChanges = async () => {
    if (!isValid) {
      return;
    }

    try {
      await Promise.all(categoriesToDelete.map((id) => deleteCategory({ id })));

      await Promise.all(
        Object.entries(editedCategories).map(async ([id, name]) => {
          const originalCategory = categories.find((category) => category.id === Number(id));

          if (originalCategory && originalCategory.name !== name) {
            await editCategory({ id: Number(id), name });
          }
        }),
      );

      await Promise.all(newCategories.map((category) => postCategory({ name: category.name })));

      resetState();

      toggleModal();
    } catch (error) {
      console.error((error as CustomError).detail);
    }
  };

  const handleCancelEditWithReset = () => {
    resetState();
    handleCancelEdit();
  };

  return (
    <Modal isOpen={isOpen} toggleModal={handleCancelEditWithReset} size='small'>
      <Modal.Header>
        <Heading.XSmall color={theme.color.light.secondary_900}>카테고리 편집</Heading.XSmall>
      </Modal.Header>
      <Modal.Body>
        <S.EditCategoryItemList>
          {categories.map(({ id, name }) => (
            <S.EditCategoryItem key={id} hasError={invalidIds.includes(id)}>
              {categoriesToDelete.includes(id) ? (
                <>
                  <Flex align='center' width='100%' height='2.5rem'>
                    <Text.Medium color={theme.color.light.analogous_primary_400} textDecoration='line-through'>
                      {name}
                    </Text.Medium>
                  </Flex>
                  <S.IconButtonWrapper>
                    <PencilIcon
                      width={20}
                      height={20}
                      aria-label='카테고리 이름 변경'
                      style={{ visibility: 'hidden' }}
                    />
                  </S.IconButtonWrapper>
                  <S.IconButtonWrapper onClick={() => handleRestoreClick(id)}>
                    <SpinArrowIcon width={16} height={16} aria-label='카테고리 복구' />
                  </S.IconButtonWrapper>
                </>
              ) : (
                <>
                  <Flex align='center' width='100%' height='2.5rem'>
                    {editingCategoryId === id ? (
                      <Input size='large' variant='outlined' style={{ width: '100%', height: '38px' }}>
                        <Input.TextField
                          type='text'
                          value={editedCategories[id] ?? name}
                          onChange={(e) => handleNameInputChange(id, e.target.value)}
                          onBlur={() => handleNameInputBlur(id)}
                          onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                              handleNameInputBlur(id);
                            }
                          }}
                          autoFocus
                        />
                      </Input>
                    ) : (
                      <Text.Medium color={theme.color.light.secondary_700}>
                        {editedCategories[id] !== undefined ? editedCategories[id] : name}
                      </Text.Medium>
                    )}
                  </Flex>
                  <S.IconButtonContainer>
                    <S.IconButtonWrapper onClick={() => handleEditClick(id)}>
                      <PencilIcon width={20} height={20} aria-label='카테고리 이름 변경' />
                    </S.IconButtonWrapper>
                    <S.IconButtonWrapper onClick={() => handleDeleteClick(id)}>
                      <TrashcanIcon width={20} height={20} aria-label='카테고리 삭제' />
                    </S.IconButtonWrapper>
                  </S.IconButtonContainer>
                </>
              )}
            </S.EditCategoryItem>
          ))}

          {newCategories.map(({ id, name }) => (
            <S.EditCategoryItem key={id} hasError={invalidIds.includes(id)}>
              <Flex align='center' width='100%' height='2.5rem'>
                {editingCategoryId === id ? (
                  <Input size='large' variant='outlined' style={{ width: '100%', height: '38px' }}>
                    <Input.TextField
                      type='text'
                      value={name}
                      onChange={(e) => handleNameInputChange(id, e.target.value)}
                      onBlur={() => handleNameInputBlur(id)}
                      onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                          handleNameInputBlur(id);
                        }
                      }}
                      autoFocus
                    />
                  </Input>
                ) : (
                  <Text.Medium color={theme.color.light.secondary_700}>{name}</Text.Medium>
                )}
              </Flex>
              <S.IconButtonContainer>
                <S.IconButtonWrapper onClick={() => handleEditClick(id)}>
                  <PencilIcon width={20} height={20} aria-label='카테고리 이름 변경' />
                </S.IconButtonWrapper>
                <S.IconButtonWrapper onClick={() => handleDeleteClick(id)}>
                  <TrashcanIcon width={20} height={20} aria-label='카테고리 삭제' />
                </S.IconButtonWrapper>
              </S.IconButtonContainer>
            </S.EditCategoryItem>
          ))}
        </S.EditCategoryItemList>
        <S.EditCategoryItem>
          <Button fullWidth variant='text' onClick={handleAddCategory} disabled={!isValid}>
            {'+ 카테고리 추가'}
          </Button>
        </S.EditCategoryItem>
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
