import { LikeIcon } from '@/assets/images';
import { Text } from '@/components';
import { theme } from '@/style/theme';
import { formatWithK } from '@/utils';

import * as S from './LikeButton.style';

interface Props {
  likesCount: number;
  isLiked: boolean;
  onLikeButtonClick: () => void;
}

const LikeCounter = ({ likesCount, isLiked, onLikeButtonClick }: Props) => (
  <S.LikeButtonContainer isLiked={isLiked} onClick={onLikeButtonClick}>
    <LikeIcon state={isLiked ? 'like' : 'unlike'} size={32} />
    <Text.Medium color={theme.color.light.secondary_800}>{formatWithK(likesCount)}</Text.Medium>
  </S.LikeButtonContainer>
);

export default LikeCounter;
