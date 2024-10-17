import { useCategoryListQuery } from '@/queries/categories';

import { CategoryFilterMenu } from '..';
import * as S from './CategoryListSection.style';

interface Props {
  onSelectCategory: (selectedCategoryId: number) => void;
}

const CategoryListSection = ({ onSelectCategory }: Props) => {
  const { data: categoryData } = useCategoryListQuery();
  const categoryList = categoryData?.categories || [];

  return (
    <S.CategoryListSectionContainer>
      <CategoryFilterMenu categoryList={categoryList} onSelectCategory={onSelectCategory} />
    </S.CategoryListSectionContainer>
  );
};

export default CategoryListSection;
