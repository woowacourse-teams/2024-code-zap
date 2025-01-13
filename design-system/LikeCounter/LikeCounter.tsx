import { LikeIcon } from '@/assets/images';
import { Text } from '@/components';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import { formatWithK } from '@/utils';

import * as S from './LikeCounter.style';

interface Props {
  likesCount: number;
  isLiked: boolean;
}

const LikeCounter = ({ likesCount, isLiked }: Props) => (
  <S.LikeCounterContainer>
    <LikeIcon state={isLiked ? 'like' : 'unClickable'} size={ICON_SIZE.X_SMALL} />
    <Text.Small color={theme.color.light.secondary_800}>{formatWithK(likesCount)}</Text.Small>
  </S.LikeCounterContainer>
);

export default LikeCounter;
