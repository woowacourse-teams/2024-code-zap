import { Guide, Text } from '@/components';
import { useTranslation } from 'react-i18next';

import { theme } from '@design/style/theme';

interface Props {
  isOpen: boolean;
  categoryErrorMessage: string;
}

const CategoryGuide = ({ isOpen, categoryErrorMessage }: Props) => {
  const { t } = useTranslation();
  const isError = categoryErrorMessage !== '';

  return (
    <Guide isOpen={isOpen} aria-hidden={!isOpen}>
      {isError ? (
        <Text.Small color={theme.color.light.analogous_primary_300}>
          {categoryErrorMessage}
        </Text.Small>
      ) : (
        <Text.Small color={theme.color.light.secondary_400}>
          {t('Category:guide.enterToRegister')}
        </Text.Small>
      )}
    </Guide>
  );
};

export default CategoryGuide;
