import { Text } from '@/components';
import { theme } from '@/style/theme';
import * as S from './TagButton.style';

interface Props {
  name: string;
  isFocused?: boolean;
  disabled?: boolean;
  onClick?: () => void;
}

const TagButton = ({ name, isFocused = false, disabled, onClick }: Props) => (
  <S.TagButtonWrapper isFocused={isFocused} disabled={disabled} onClick={() => onClick && onClick()}>
    <Text.Small color={isFocused ? theme.color.light.white : theme.color.light.secondary_700}>{name}</Text.Small>
  </S.TagButtonWrapper>
);

export default TagButton;
