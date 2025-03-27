import { Input, LoadingBall } from '@/components';
import { useLoaderDelay } from '@/hooks';
import { useTranslation } from 'react-i18next';

import { theme } from '@design/style/theme';

interface Props {
  value: string;
  onEnterDown: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  onChange: (
    e: React.ChangeEvent<HTMLInputElement>,
    compareValue?: string,
  ) => void;
  isPending: boolean;
}

const NewCategoryInput = ({
  value,
  onChange,
  onEnterDown,
  isPending,
}: Props) => {
  const { t } = useTranslation();
  const showLoader = useLoaderDelay(isPending, 700);

  return (
    <Input
      size='medium'
      variant='outlined'
      inputColor={theme.color.light.secondary_400}
    >
      {showLoader ? (
        <LoadingBall />
      ) : (
        <Input.TextField
          autoFocus
          placeholder={t('Category:input.newCategoryPlaceholder')}
          value={value}
          onChange={onChange}
          onKeyDown={onEnterDown}
          placeholderColor={theme.color.light.secondary_600}
        />
      )}
    </Input>
  );
};

export default NewCategoryInput;
