import { Text } from '@/components';
import { Category } from '@/types/template';
import * as S from './CategoryButton.style';

interface Props {
  category: Category;
  onClick: () => void;
}

const CategoryButton = ({ category, onClick }: Props) => {
  const handleCategoryButtonClick = () => {
    onClick();
  };

  return (
    <S.CategoryButtonWrapper onClick={handleCategoryButtonClick}>
      <Text.Body color='#393E46' weight='bold'>
        {category.name}
      </Text.Body>
    </S.CategoryButtonWrapper>
  );
};

export default CategoryButton;
