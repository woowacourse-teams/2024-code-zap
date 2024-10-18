import { Heading } from '@/components';
import { theme } from '@/style/theme';

import * as S from './TopBanner.style';

interface Props {
  name: string;
}

const TopBanner = ({ name }: Props) => (
  <S.TopBannerContainer>
    <S.TopBannerTextWrapper>
      <Heading.Medium color={theme.color.light.black}>{name}</Heading.Medium>
      <Heading.XSmall color={theme.color.light.black} weight='regular'>
        {`${name ? '님' : ''}의 템플릿 입니다 :)`}
      </Heading.XSmall>
    </S.TopBannerTextWrapper>
  </S.TopBannerContainer>
);

export default TopBanner;
