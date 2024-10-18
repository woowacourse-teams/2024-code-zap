import { PlusIcon } from '@/assets/images';
import { Text } from '@/components';
import { useCustomNavigate } from '@/hooks';
import { END_POINTS } from '@/routes';
import { theme } from '@/style/theme';

import * as S from './NewTemplateButton.style';

const NewTemplateButton = () => {
  const navigate = useCustomNavigate();

  return (
    <S.NewTemplateButtonWrapper onClick={() => navigate(END_POINTS.TEMPLATES_UPLOAD)}>
      <PlusIcon width={24} height={24} aria-label='새 템플릿' />
      <Text.Large color={theme.color.light.primary_500} weight='bold'>
        이곳을 눌러 새 템플릿을 추가해보세요 :)
      </Text.Large>
    </S.NewTemplateButtonWrapper>
  );
};

export default NewTemplateButton;
