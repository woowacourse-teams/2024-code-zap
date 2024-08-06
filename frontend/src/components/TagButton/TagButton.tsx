import { Text } from '@/components';
import { theme } from '@/style/theme';
import * as S from './TagButton.style';

interface Props {
  id: number;
  name: string;
  disabled?: boolean;
  onClick?: (id: number) => void;
}

const TagButton = ({ id, name, disabled, onClick }: Props) => (
  <S.TagButtonWrapper
    bgColor={theme.color.light.tertiary_50}
    borderColor={theme.color.light.tertiary_200}
    disabled={disabled}
    onClick={() => onClick && onClick(id)}
  >
    <Text.Small color={theme.color.light.secondary_700}>{name}</Text.Small>
  </S.TagButtonWrapper>
);

export default TagButton;
