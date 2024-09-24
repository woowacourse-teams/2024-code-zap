import { LikeIcon } from '@/assets/images';
import { Text } from '@/components';
import { theme } from '@/style/theme';
import { formatWithK } from '@/utils';

import * as S from './LikeWidget.style';

interface Props {
  likesCount: number;
  isLiked: boolean;
  clickable?: boolean;
  onLikeWidgetClick?: () => void;
}

const LikeWidget = ({ likesCount, isLiked, clickable = false, onLikeWidgetClick }: Props) => (
  <S.LikeButtonWidgetContainer isLiked={isLiked} clickable={clickable} onClick={onLikeWidgetClick}>
    <LikeIcon state={isLiked ? 'like' : clickable ? 'unlike' : 'unClickable'} size={14} />
    <Text.Small color={theme.color.light.secondary_800}>{formatWithK(likesCount)}</Text.Small>
  </S.LikeButtonWidgetContainer>
);

export default LikeWidget;
