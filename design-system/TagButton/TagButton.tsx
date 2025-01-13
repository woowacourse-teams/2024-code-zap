import { XSignIcon } from '@/assets/images';
import { Text } from '@/components';
import { TAG_COLORS, INPUT_TAG_COLOR } from '@/style/tagColors';
import { theme } from '@/style/theme';

import * as S from './TagButton.style';

interface Props {
  id?: number;
  name: string;
  isFocused?: boolean;
  disabled?: boolean;
  variant?: 'default' | 'edit';
  onClick?: () => void;
}

const getTagColor = (id?: number) => (id ? TAG_COLORS[id % TAG_COLORS.length] : INPUT_TAG_COLOR);

const TagButton = ({ id, name, isFocused = false, disabled = false, variant = 'default', onClick }: Props) => {
  const { background, border } = getTagColor(id);

  return (
    <S.TagButtonWrapper
      background={background}
      border={border}
      isFocused={isFocused}
      disabled={disabled}
      onClick={() => onClick && onClick()}
    >
      <Text.Medium color={theme.color.light.secondary_800}>{name}</Text.Medium>
      {variant === 'edit' && <XSignIcon width={10} height={10} aria-label='태그 삭제' />}
    </S.TagButtonWrapper>
  );
};

export default TagButton;
