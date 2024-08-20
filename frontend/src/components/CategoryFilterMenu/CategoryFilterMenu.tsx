import { useMemo, useState } from 'react';

import { SettingIcon } from '@/assets/images';
import { Text, CategoryEditModal } from '@/components';
import { useToggle } from '@/hooks/utils';
import type { Category } from '@/types';
import { theme } from '../../style/theme';
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
      <S.IconButtonWrapper onClick={toggleEditModal}>
        <SettingIcon width={18} height={18} aria-label='카테고리 편집' />
      </S.IconButtonWrapper>
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
        handleCancelEdit={toggleEditModal}
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
  <S.CategoryButtonWrapper data-testid='category-button' disabled={disabled} onClick={onClick}>
    <Text.Medium color={theme.color.light.secondary_700} weight='bold'>
      {name}
    </Text.Medium>
  </S.CategoryButtonWrapper>
);

export default CategoryFilterMenu;
