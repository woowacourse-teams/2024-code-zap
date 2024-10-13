import { Flex } from '@/components';
import { useCategoryListQuery } from '@/queries/categories';

import { CategoryFilterMenu } from '..';

interface Props {
  onSelectCategory: (selectedCategoryId: number) => void;
}

const CategoryListSection = ({ onSelectCategory }: Props) => {
  const { data: categoryData } = useCategoryListQuery();
  const categoryList = categoryData?.categories || [];

  return (
    <Flex direction='column' gap='2.5rem' style={{ marginTop: '4.5rem' }}>
      <CategoryFilterMenu categoryList={categoryList} onSelectCategory={onSelectCategory} />
    </Flex>
  );
};

export default CategoryListSection;
