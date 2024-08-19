// CategoryEditModal.tsx
import { useState } from 'react';

import { TrashcanIcon, SpinArrowIcon, PencilIcon } from '@/assets/images';
import { Heading, Text, Modal, Input, Flex, Button } from '@/components';
import { useCategoryDeleteMutation, useCategoryEditMutation } from '@/queries/category';
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
  const [editingCategoryId, setEditingCategoryId] = useState<number | null>(null);

  const { mutateAsync: editCategory } = useCategoryEditMutation();
  const { mutateAsync: deleteCategory } = useCategoryDeleteMutation(categories);

  const handleNameInputChange = (id: number, name: string) => {
    setEditedCategories((prev) => ({ ...prev, [id]: name }));
  };

  const handleDeleteClick = (id: number) => {
    setCategoriesToDelete((prev) => [...prev, id]);
  };

  const handleRestoreClick = (id: number) => {
    setCategoriesToDelete((prev) => prev.filter((categoryId) => categoryId !== id));
  };

  const handleEditClick = (id: number) => {
    setEditingCategoryId(id);
  };

  const handleNameInputBlur = () => {
    setEditingCategoryId(null);
  };

  const handleSaveChanges = async () => {
    await Promise.all(
      Object.entries(editedCategories).map(async ([id, name]) => {
        const originalCategory = categories.find((category) => category.id === Number(id));

        if (originalCategory && originalCategory.name !== name) {
          await editCategory({ id: Number(id), name });
        }
      }),
    );

    try {
      await Promise.all(categoriesToDelete.map((id) => deleteCategory({ id })));
      toggleModal();
    } catch (error) {
      console.error((error as CustomError).detail);
    }
  };

  const handleCancelEditWithReset = () => {
    setEditedCategories({});
    setCategoriesToDelete([]);
    setEditingCategoryId(null);
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
            <S.EditCategoryItem key={id}>
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
                          onBlur={() => handleNameInputBlur()}
                          onKeyDown={(e) => {
                            if (e.key === 'Enter') {
                              handleNameInputBlur();
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
        </S.EditCategoryItemList>
      </Modal.Body>
      <Flex justify='flex-end' gap='1rem'>
        <Button variant='outlined' onClick={handleCancelEditWithReset}>
          취소
        </Button>
        <Button onClick={handleSaveChanges}>저장</Button>
      </Flex>
    </Modal>
  );
};

export default CategoryEditModal;
