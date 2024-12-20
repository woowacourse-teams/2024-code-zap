import { CategoryFilterMenu } from '@/pages/MemberTemplatePage/components';
import { useCategoryListQuery } from '@/queries/categories';

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
      <CategoryFilterMenu memberId={memberId} categoryList={categoryList} onSelectCategory={onSelectCategory} />
    </S.CategoryListSectionContainer>
  );
};

export default CategoryListSection;
