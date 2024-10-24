import { PersonIcon } from '@/assets/images';
import { Text } from '@/components';
import { theme } from '@/style/theme';

import * as S from './AuthorInfo.style';

interface Props {
  memberName: string;
}

const AuthorInfo = ({ memberName }: Props) => (
  <S.AuthorInfoContainer>
    <PersonIcon width={17} height={17} color={theme.color.light.primary_500} />
    <S.EllipsisTextWrapper>
      <Text.Small color={theme.color.light.primary_500}>{memberName}</Text.Small>
    </S.EllipsisTextWrapper>
  </S.AuthorInfoContainer>
);

export default AuthorInfo;
