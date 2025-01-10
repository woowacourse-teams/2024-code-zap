import { css } from '@emotion/react';
import { PropsWithChildren, useEffect, useRef, useState } from 'react';

import { PencilIcon, SpinArrowIcon, TrashcanIcon, DragIcon } from '@/assets/images';
import { Text, Modal, Input, Flex, Button } from '@/components';
import { useCategoryNameValidation } from '@/hooks/category';
import { useCategoryEditMutation } from '@/queries/categories';
import { validateCategoryName } from '@/service/validates';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import type { Category } from '@/types';

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
    } else {
      const ordinal = editedCategories.find((category) => category.id === id)?.ordinal as number;

      setEditedCategories((prev) => [...prev, { id, name, ordinal }]);
    }
  };

  const handleDrag = (categories: Category[]) => {
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

    const ordinal = categories.length + newCategories.length;

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
            handleDrag={handleDrag}
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

interface ExistingCategoryItemProps {
  id: number;
  name: string;
  isEditing: boolean;
  isDeleted: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
  onRestoreClick: (id: number) => void;
}

const ExistingCategoryItem = ({
  id,
  name,
  isEditing,
  isDeleted,
  onChange,
  onBlur,
  onKeyDown,
  onEditClick,
  onDeleteClick,
  onRestoreClick,
}: ExistingCategoryItemProps) => (
  <>
    {isDeleted ? (
      <>
        <CategoryName>
          <Text.Medium color={theme.color.light.analogous_primary_400} textDecoration='line-through'>
            {name}
          </Text.Medium>
        </CategoryName>
        <IconButtons restore onRestoreClick={() => onRestoreClick(id)} />
      </>
    ) : (
      <>
        <CategoryName>
          {isEditing ? (
            <CategoryNameInput value={name} onChange={onChange} onBlur={onBlur} onKeyDown={onKeyDown} />
          ) : (
            <Text.Medium color={theme.color.light.secondary_500} weight='bold'>
              {name}
            </Text.Medium>
          )}
        </CategoryName>
        <IconButtons edit delete onEditClick={() => onEditClick(id)} onDeleteClick={() => onDeleteClick(id)} />
      </>
    )}
  </>
);

interface NewCategoryItemProps {
  id: number;
  name: string;
  isEditing: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
}

const NewCategoryItem = ({
  id,
  name,
  isEditing,
  onChange,
  onBlur,
  onKeyDown,
  onEditClick,
  onDeleteClick,
}: NewCategoryItemProps) => (
  <>
    <CategoryName>
      {isEditing ? (
        <CategoryNameInput value={name} onChange={onChange} onBlur={onBlur} onKeyDown={onKeyDown} />
      ) : (
        <Text.Medium color={theme.color.light.secondary_500} weight='bold'>
          {name}
        </Text.Medium>
      )}
    </CategoryName>
    <IconButtons edit delete onEditClick={() => onEditClick(id)} onDeleteClick={() => onDeleteClick(id)} />
  </>
);

const CategoryName = ({ children }: PropsWithChildren) => (
  <Flex align='center' width='100%' height='2.5rem'>
    <DragIcon color={theme.color.light.secondary_400} css={{ marginRight: '0.5rem' }} />
    {children}
  </Flex>
);

interface CategoryNameInputProps {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
}

const CategoryNameInput = ({ value, onChange, onBlur, onKeyDown }: CategoryNameInputProps) => (
  <Input size='large' variant='text' style={{ width: '100%', height: '2.375rem' }}>
    <Input.TextField
      type='text'
      value={value}
      placeholder='카테고리 입력'
      onChange={onChange}
      onBlur={onBlur}
      onKeyDown={onKeyDown}
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
        <PencilIcon width={ICON_SIZE.MEDIUM_LARGE} height={ICON_SIZE.MEDIUM_LARGE} aria-label='카테고리 이름 변경' />
      </S.IconButtonWrapper>
    )}
    {del && (
      <S.IconButtonWrapper onClick={onDeleteClick}>
        <TrashcanIcon width={ICON_SIZE.MEDIUM_LARGE} height={ICON_SIZE.MEDIUM_LARGE} aria-label='카테고리 삭제' />
      </S.IconButtonWrapper>
    )}
  </S.IconButtonContainer>
);

export default CategoryEditModal;
