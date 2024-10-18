import { useCategoryListQuery } from '@/queries/categories';

import { CategoryFilterMenu } from '..';
import * as S from './CategoryListSection.style';

interface Props {
  memberId: number;
  onSelectCategory: (selectedCategoryId: number) => void;
}

const CategoryListSection = ({ onSelectCategory, memberId }: Props) => {
  const { data: categoryData } = useCategoryListQuery({ memberId });
  const categoryList = categoryData?.categories || [];

  return (
    <S.CategoryListSectionContainer>
      <CategoryFilterMenu categoryList={categoryList} onSelectCategory={onSelectCategory} />
    </S.CategoryListSectionContainer>
  );
};

export default CategoryListSection;
