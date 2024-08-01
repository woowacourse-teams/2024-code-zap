import { Text } from '@/components';
import { Category } from '@/types/template';
import { theme } from '../../style/theme';
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
      <Text.Medium color={theme.color.light.secondary_700} weight='bold'>
        {category.name}
      </Text.Medium>
    </S.CategoryButtonWrapper>
  );
};

export default CategoryButton;
