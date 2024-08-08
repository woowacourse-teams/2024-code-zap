import { useMemo, useState } from 'react';

import { Text } from '@/components';
import { theme } from '@/style/theme';
import type { Category } from '@/types';
import * as S from './CategoryFilterMenu.style';

interface MenuProps {
  categories: Category[];
  onSelectCategory: (selectedCategoryId: number) => void;
}

interface ButtonProps {
  name: string;
  disabled: boolean;
  onClick: () => void;
}

const CategoryFilterMenu = ({ categories, onSelectCategory }: MenuProps) => {
  const [selectedId, setSelectedId] = useState<number>(0);

  const reorderedCategories = useMemo(() => {
    if (categories.length === 0) {
      return [];
    }

    const [first, ...rest] = categories;

    return [...rest, first];
  }, [categories]);

  const indexById: Record<number, number> = useMemo(() => {
    const map: Record<number, number> = {
      0: 0,
    };

    reorderedCategories.forEach(({ id }, index) => {
      map[id] = index + 1;
    });

    return map;
  }, [reorderedCategories]);

  const handleButtonClick = (id: number) => {
    setSelectedId(id);
    onSelectCategory(id);
  };

  return (
    <S.CategoryContainer>
      <CategoryButton name='전체보기' disabled={selectedId === 0} onClick={() => handleButtonClick(0)} />

      {reorderedCategories.map(({ id, name }) => (
        <CategoryButton key={id} name={name} disabled={selectedId === id} onClick={() => handleButtonClick(id)} />
      ))}

      <S.HighlightBox selectedIndex={indexById[selectedId]} categoryCount={reorderedCategories.length + 1} />
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

export default CategoryFilterMenu;
