import { useMemo, useState } from 'react';

import { BooksIcon, Chevron2Icon, SettingIcon } from '@/assets/images';
import { Text } from '@/components';
import { useToggle, useWindowWidth } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { CategoryEditModal } from '@/pages/MemberTemplatePage/components';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import type { Category } from '@/types';

import * as S from './CategoryFilterMenu.style';

interface CategoryMenuProps {
  memberId: number;
  categoryList: Category[];
  onSelectCategory: (selectedCategoryId: number) => void;
}

const CategoryFilterMenu = ({ memberId, categoryList, onSelectCategory }: CategoryMenuProps) => {
  const [selectedId, setSelectedId] = useState<number>(0);
  const [isEditModalOpen, toggleEditModal] = useToggle();
  const [isMenuOpen, toggleMenu] = useToggle(false);
  const windowWidth = useWindowWidth();

  const {
    memberInfo: { memberId: currentMemberId },
  } = useAuth();

  const handleCategorySelect = (id: number) => {
    setSelectedId(id);
    onSelectCategory(id);
    if (windowWidth <= 768) {
      toggleMenu();
    }
  };

  const handleCategoryDelete = (deletedIds: number[]) => {
    if (deletedIds.includes(selectedId)) {
      setSelectedId(0);
      onSelectCategory(0);
    }
  };

  const [defaultCategory, ...userCategories] = categoryList.length ? categoryList : [{ id: 0, name: '' }];

  const indexById: Record<number, number> = useMemo(() => {
    const map: Record<number, number> = { 0: 0, [defaultCategory.id]: categoryList.length };

    userCategories.forEach(({ id }, index) => {
      map[id] = index + 1;
    });

    return map;
  }, [categoryList.length, defaultCategory.id, userCategories]);

  return (
    <>
      {windowWidth <= 768 && (
        <S.ToggleMenuButton onClick={toggleMenu} isMenuOpen={isMenuOpen}>
          <BooksIcon width={ICON_SIZE.X_LARGE} height={ICON_SIZE.X_LARGE} aria-label='카테고리 메뉴 열기' />
          <Chevron2Icon width={ICON_SIZE.LARGE} height={ICON_SIZE.LARGE} />
        </S.ToggleMenuButton>
      )}
      <S.CategoryContainer isMenuOpen={isMenuOpen}>
        {memberId === currentMemberId && (
          <S.IconButtonWrapper onClick={toggleEditModal} isMenuOpen={isMenuOpen}>
            <SettingIcon width={ICON_SIZE.MEDIUM_LARGE} height={ICON_SIZE.MEDIUM_LARGE} aria-label='카테고리 편집' />
          </S.IconButtonWrapper>
        )}

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
            categoryCount={categoryList.length}
            isMenuOpen={isMenuOpen}
          />
        </S.CategoryListContainer>

        <CategoryEditModal
          isOpen={isEditModalOpen}
          toggleModal={toggleEditModal}
          categories={userCategories}
          handleCancelEdit={toggleEditModal}
          onDeleteCategory={handleCategoryDelete}
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
