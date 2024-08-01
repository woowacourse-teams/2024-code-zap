import { Text } from '@/components';
import * as S from './TagButton.style';

interface Props {
  id: number;
  name: string;
  color?: string;
  disabled?: boolean;
  onClick?: (id: number) => void;
}

const TagButton = ({ id, name, color = '#E5EBF5', disabled, onClick }: Props) => (
  <S.TagButtonWrapper bgColor={color} borderColor='#393E46' disabled={disabled} onClick={() => onClick && onClick(id)}>
    <Text.Caption color='#393E46'>{name}</Text.Caption>
  </S.TagButtonWrapper>
);

export default TagButton;
