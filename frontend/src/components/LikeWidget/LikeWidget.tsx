import { LikeIcon } from '@/assets/images';
import { Text } from '@/components';
import { theme } from '@/style/theme';
import { formatWithK } from '@/utils';

import * as S from './LikeWidget.style';

interface Props {
  likeCount: number;
  isLiked: boolean;
  clickable?: boolean;
  onLikeButtonClick?: () => void;
}

const LikeWidget = ({ likeCount, isLiked, clickable = false, onLikeButtonClick }: Props) => {
  console.log('temp');

  return (
    <S.LikeButtonWidgetContainer isLiked={isLiked} clickable={clickable} onClick={onLikeButtonClick}>
      <LikeIcon state={isLiked ? 'like' : clickable ? 'unlike' : 'unClickable'} size={14} />
      <Text.Small color={theme.color.light.secondary_800}>{formatWithK(likeCount)}</Text.Small>
    </S.LikeButtonWidgetContainer>
  );
};

export default LikeWidget;
