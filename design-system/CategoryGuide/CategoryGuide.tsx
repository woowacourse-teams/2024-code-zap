import { Guide, Text } from '@/components';
import { theme } from '@design/style/theme';

interface Props {
  isOpen: boolean;
  categoryErrorMessage: string;
}

const CategoryGuide = ({ isOpen, categoryErrorMessage }: Props) => {
  const isError = categoryErrorMessage !== '';

  return (
    <Guide isOpen={isOpen} aria-hidden={!isOpen}>
      {isError ? (
        <Text.Small color={theme.color.light.analogous_primary_300}>
          {categoryErrorMessage}
        </Text.Small>
      ) : (
        <Text.Small color={theme.color.light.secondary_400}>
          엔터로 카테고리를 등록해요
        </Text.Small>
      )}
    </Guide>
  );
};

export default CategoryGuide;
