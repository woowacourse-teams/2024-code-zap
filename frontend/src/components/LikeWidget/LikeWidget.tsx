import LikeIcon from '../../assets/images/like';
import { theme } from '../../style/theme';
import Text from '../Text/Text';
import * as S from './LikeWidget.style';

interface Props {
  likeCount: string;
  isLiked: boolean;
  clickable?: boolean;
  onLikeButtonClick?: () => void;
}

const LikeWidget = ({ likeCount, isLiked, clickable, onLikeButtonClick }: Props) => {
  console.log('temp');

  return (
    <S.LikeButtonWidgetContainer isLiked={isLiked} onClick={onLikeButtonClick}>
      <LikeIcon state={isLiked ? 'like' : clickable ? 'unlike' : 'unClickable'} size={14} />
      <Text.Small color={theme.color.light.secondary_800}>{likeCount}</Text.Small>
    </S.LikeButtonWidgetContainer>
  );
};

export default LikeWidget;
