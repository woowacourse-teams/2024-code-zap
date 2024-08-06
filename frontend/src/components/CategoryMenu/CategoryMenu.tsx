import { useState } from 'react';

import { CATEGORY } from '@/api';
import { Text } from '@/components';
import { theme } from '@/style/theme';
import type { Category } from '@/types';
import * as S from './CategoryMenu.style';

interface MenuProps {
  categories: Category[];
  onSelectCategory: (categoryId: number) => void;
}

interface ButtonProps {
  name: string;
  disabled: boolean;
  onClick: () => void;
}

const CategoryMenu = ({ categories, onSelectCategory }: MenuProps) => {
  const [selectedId, setSelectedId] = useState<number>(0);

  const indexById: Record<number, number> = {
    0: 0,
    1: categories.length + 1,
  };

  const handleButtonClick = (id: number) => {
    setSelectedId(id);
    onSelectCategory(id);
  };

  return (
    <S.CategoryContainer>
      <CategoryButton name='전체보기' disabled={selectedId === 0} onClick={() => handleButtonClick(0)} />

      {categories.map(({ id, name }, index) => {
        indexById[id] = index + 1;

        return (
          <CategoryButton key={id} name={name} disabled={selectedId === id} onClick={() => handleButtonClick(id)} />
        );
      })}

      <CategoryButton
        name='카테고리 미지정'
        disabled={selectedId === CATEGORY.UNASSIGNED_ID}
        onClick={() => handleButtonClick(CATEGORY.UNASSIGNED_ID)}
      />

      <S.HighlightBox selectedIndex={indexById[selectedId]} categoryCount={categories.length + 2} />
    </S.CategoryContainer>
  );
};

const CategoryButton = ({ name, disabled, onClick }: ButtonProps) => (
  <S.CategoryButtonWrapper disabled={disabled} onClick={onClick}>
    <Text.Medium color={theme.color.light.secondary_700} weight='bold'>
      {name}
    </Text.Medium>
  </S.CategoryButtonWrapper>
);

export default CategoryMenu;
