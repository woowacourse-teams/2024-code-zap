import { css } from '@emotion/react';
import { useState } from 'react';

import { Text, Modal, Input, Flex, Button } from '@/components';
import { useCategoryNameValidation } from '@/hooks/category';
import { useCategoryDeleteMutation, useCategoryEditMutation, useCategoryUploadMutation } from '@/queries/category';
import type { Category, CustomError } from '@/types';
import { PencilIcon, SpinArrowIcon, TrashcanIcon } from '../../assets/images/index';
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
    const newCategoryId = categories[categories.length - 1].id + newCategories.length + 1;

    setNewCategories((prev) => [...prev, { id: newCategoryId, name: '' }]);
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
      <Modal.Header>{'카테고리 편집'}</Modal.Header>
      <Modal.Body>
        <S.EditCategoryItemList>
          <CategoryItems
            categories={categories}
            newCategories={newCategories}
            editedCategories={editedCategories}
            categoriesToDelete={categoriesToDelete}
            editingCategoryId={editingCategoryId}
            invalidIds={invalidIds}
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

interface CategoryItemsProps {
  categories: Category[];
  newCategories: { id: number; name: string }[];
  editedCategories: Record<number, string>;
  categoriesToDelete: number[];
  editingCategoryId: number | null;
  invalidIds: number[];
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
  onEditClick,
  onDeleteClick,
  onRestoreClick,
  onNameInputChange,
  onNameInputBlur,
}: CategoryItemsProps) => (
  <>
    {categories.map(({ id, name }) => (
      <S.EditCategoryItem key={id} hasError={invalidIds.includes(id)}>
        {categoriesToDelete.includes(id) ? (
          // 기존 : 삭제 상태
          <>
            <Flex align='center' width='100%' height='2.5rem'>
              <Text.Medium color={theme.color.light.analogous_primary_400} textDecoration='line-through'>
                {name}
              </Text.Medium>
            </Flex>
            <IconButtons restore onRestoreClick={() => onRestoreClick(id)} />
          </>
        ) : (
          <>
            <Flex align='center' width='100%' height='2.5rem'>
              {editingCategoryId === id ? (
                // 기존 : 수정 상태
                <Input size='large' variant='text' style={{ width: '100%', height: '2.375rem' }}>
                  <Input.TextField
                    type='text'
                    value={editedCategories[id] ?? name}
                    placeholder='카테고리 입력'
                    onChange={(e) => onNameInputChange(id, e.target.value)}
                    onBlur={() => onNameInputBlur(id)}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter') {
                        onNameInputBlur(id);
                      }
                    }}
                    autoFocus
                    css={css`
                      font-weight: bold;
                      &::placeholder {
                        font-weight: normal;
                        color: ${theme.color.light.secondary_400};
                      }
                    `}
                  />
                </Input>
              ) : (
                // 기존 : 기본 상태
                <Text.Medium color={theme.color.light.secondary_500} weight='bold'>
                  {editedCategories[id] !== undefined ? editedCategories[id] : name}
                </Text.Medium>
              )}
            </Flex>
            <IconButtons edit delete onEditClick={() => onEditClick(id)} onDeleteClick={() => onDeleteClick(id)} />
          </>
        )}
      </S.EditCategoryItem>
    ))}

    {newCategories.map(({ id, name }) => (
      <S.EditCategoryItem key={id} hasError={invalidIds.includes(id)}>
        <Flex align='center' width='100%' height='2.5rem'>
          {editingCategoryId === id ? (
            // 생성 : 수정 상태
            <Input size='large' variant='text' style={{ width: '100%', height: '2.375rem' }}>
              <Input.TextField
                type='text'
                value={name}
                placeholder='카테고리 입력'
                onChange={(e) => onNameInputChange(id, e.target.value)}
                onBlur={() => onNameInputBlur(id)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    onNameInputBlur(id);
                  }
                }}
                autoFocus
                css={css`
                  font-weight: bold;
                  &::placeholder {
                    font-weight: normal;
                    color: ${theme.color.light.secondary_400};
                  }
                `}
              />
            </Input>
          ) : (
            // 생성 : 기본 상태
            <Text.Medium color={theme.color.light.secondary_500} weight='bold'>
              {name}
            </Text.Medium>
          )}
        </Flex>
        <IconButtons edit delete onEditClick={() => onEditClick(id)} onDeleteClick={() => onDeleteClick(id)} />
      </S.EditCategoryItem>
    ))}
  </>
);

interface IconButtonsProps {
  onRestoreClick?: () => void;
  onEditClick?: () => void;
  onDeleteClick?: () => void;
  restore?: boolean;
  edit?: boolean;
  delete?: boolean;
}

const IconButtons = ({ onRestoreClick, onEditClick, onDeleteClick, restore, edit, delete: del }: IconButtonsProps) => (
  <S.IconButtonContainer>
    {restore && (
      <S.IconButtonWrapper onClick={onRestoreClick}>
        <SpinArrowIcon aria-label='카테고리 복구' />
      </S.IconButtonWrapper>
    )}
    {edit && (
      <S.IconButtonWrapper onClick={onEditClick}>
        <PencilIcon width={20} height={20} aria-label='카테고리 이름 변경' />
      </S.IconButtonWrapper>
    )}
    {del && (
      <S.IconButtonWrapper onClick={onDeleteClick}>
        <TrashcanIcon width={20} height={20} aria-label='카테고리 삭제' />
      </S.IconButtonWrapper>
    )}
  </S.IconButtonContainer>
);

export default CategoryEditModal;
