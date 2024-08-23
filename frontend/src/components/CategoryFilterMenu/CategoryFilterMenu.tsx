import { useMemo, useState } from 'react';

import { BooksIcon, Chevron2Icon, SettingIcon } from '@/assets/images';
import { Text, CategoryEditModal } from '@/components';
import { useToggle, useWindowWidth } from '@/hooks/utils';
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
  const [isMenuOpen, toggleMenu] = useToggle(false);
  const windowWidth = useWindowWidth();

  const handleCategorySelect = (id: number) => {
    setSelectedId(id);
    onSelectCategory(id);
    if (windowWidth <= 768) {
      toggleMenu();
    }
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
    <>
      {windowWidth <= 768 && (
        <S.ToggleMenuButton onClick={toggleMenu} isMenuOpen={isMenuOpen}>
          <BooksIcon width={32} height={32} aria-label='카테고리 메뉴 열기' />
          <Chevron2Icon width={24} height={24} />
        </S.ToggleMenuButton>
      )}
      <S.CategoryContainer isMenuOpen={isMenuOpen}>
        <S.IconButtonWrapper onClick={toggleEditModal} isMenuOpen={isMenuOpen}>
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
            isMenuOpen={isMenuOpen}
          />
        </S.CategoryListContainer>

        <CategoryEditModal
          isOpen={isEditModalOpen}
          toggleModal={toggleEditModal}
          categories={userCategories}
          handleCancelEdit={toggleEditModal}
        />
      </S.CategoryContainer>
      {isMenuOpen && <S.Backdrop onClick={toggleMenu} />}
    </>
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
