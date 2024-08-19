import { useMemo, useState } from 'react';

import { SettingIcon, TrashcanIcon, SpinArrowIcon, PencilIcon } from '@/assets/images';
import { Heading, Text, Modal, Input, Flex, Button } from '@/components';
import { useToggle } from '@/hooks/utils';
import { useCategoryDeleteQuery, useCategoryEditQuery } from '@/queries/category';
import { theme } from '@/style/theme';
import type { Category } from '@/types';
import * as S from './CategoryFilterMenu.style';

interface CategoryMenuProps {
  categories: Category[];
  onSelectCategory: (selectedCategoryId: number) => void;
}

const CategoryFilterMenu = ({ categories, onSelectCategory }: CategoryMenuProps) => {
  const [selectedId, setSelectedId] = useState<number>(0);
  const [isEditModalOpen, toggleEditModal] = useToggle();

  const handleCategorySelect = (id: number) => {
    setSelectedId(id);
    onSelectCategory(id);
  };

  const [defaultCategory, ...userCategories] = categories.length ? categories : [{ id: 0, name: '' }];

  const indexById: Record<number, number> = useMemo(() => {
    const map: Record<number, number> = { 0: 0, [defaultCategory.id]: categories.length };

    userCategories.forEach(({ id }, index) => {
      map[id] = index + 1;
    });

    return map;
  }, [categories.length, defaultCategory.id, userCategories]);

  return (
    <S.CategoryContainer>
      <S.SettingButton onClick={toggleEditModal}>
        <SettingIcon width={18} height={18} aria-label='카테고리 편집' />
      </S.SettingButton>
      <S.CategoryListContainer>
        <S.CategoryButtonContainer>
          <CategoryButton name='전체보기' disabled={selectedId === 0} onClick={() => handleCategorySelect(0)} />
        </S.CategoryButtonContainer>

        {userCategories.map(({ id, name }) => (
          <S.CategoryButtonContainer key={id}>
            <CategoryButton name={name} disabled={selectedId === id} onClick={() => handleCategorySelect(id)} />
          </S.CategoryButtonContainer>
        ))}

        <S.CategoryButtonContainer>
          <CategoryButton
            name={defaultCategory.name}
            disabled={selectedId === defaultCategory.id}
            onClick={() => handleCategorySelect(defaultCategory.id)}
          />
        </S.CategoryButtonContainer>

        <S.HighlightBox
          data-testid='category-highlighter-box'
          selectedIndex={indexById[selectedId]}
          categoryCount={categories.length}
        />
      </S.CategoryListContainer>

      <CategoryEditModal
        isOpen={isEditModalOpen}
        toggleModal={toggleEditModal}
        categories={userCategories}
        defaultCategory={defaultCategory}
      />
    </S.CategoryContainer>
  );
};

interface CategoryButtonProps {
  name: string;
  disabled: boolean;
  onClick: () => void;
}

const CategoryButton = ({ name, disabled, onClick }: CategoryButtonProps) => (
  <S.CategoryButtonWrapper disabled={disabled} onClick={onClick}>
    <Text.Medium color={theme.color.light.secondary_700} weight='bold'>
      {name}
    </Text.Medium>
  </S.CategoryButtonWrapper>
);

interface CategoryEditModalProps {
  isOpen: boolean;
  toggleModal: () => void;
  categories: Category[];
  defaultCategory: Category;
}

const CategoryEditModal = ({ isOpen, toggleModal, categories }: CategoryEditModalProps) => {
  const [editedCategories, setEditedCategories] = useState<Record<number, string>>({});
  const [categoriesToDelete, setCategoriesToDelete] = useState<number[]>([]);
  const [editingCategoryId, setEditingCategoryId] = useState<number | null>(null);

  const { mutateAsync: editCategory } = useCategoryEditQuery();
  const { mutateAsync: deleteCategory } = useCategoryDeleteQuery();

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
    await Promise.all(categoriesToDelete.map((id) => deleteCategory({ id })));
    toggleModal();
  };

  const handleCancelEdit = () => {
    setEditedCategories({});
    setCategoriesToDelete([]);
    setEditingCategoryId(null);
    toggleModal();
  };

  return (
    <Modal isOpen={isOpen} toggleModal={toggleModal} size='small'>
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
                      width={18}
                      height={18}
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
                      <PencilIcon width={18} height={18} aria-label='카테고리 이름 변경' />
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
        <Button variant='outlined' onClick={handleCancelEdit}>
          취소
        </Button>
        <Button onClick={handleSaveChanges}>저장</Button>
      </Flex>
    </Modal>
  );
};

export default CategoryFilterMenu;
