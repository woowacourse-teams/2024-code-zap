import { theme } from '@design/style/theme';
import { useTranslation } from 'react-i18next';

import { Heading } from '@/components';
import { useWindowWidth } from '@/hooks';
import { BREAKING_POINT } from '@/style/styleConstants';

import * as S from './TopBanner.style';

interface Props {
  name: string;
}

const TopBanner = ({ name }: Props) => {
  const windowWidth = useWindowWidth();
  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;
  const { t } = useTranslation('MemberTemplatePage');

  return (
    <S.TopBannerContainer>
      {isMobile ? (
        <S.TopBannerTextWrapper>
          <Heading.XSmall color={theme.color.light.black}>
            {name}
          </Heading.XSmall>
          <Heading.XSmall color={theme.color.light.black} weight='regular'>
            {t('topBanner.message')}
          </Heading.XSmall>
        </S.TopBannerTextWrapper>
      ) : (
        <S.TopBannerTextWrapper>
          <Heading.Medium color={theme.color.light.black}>
            {name}
          </Heading.Medium>
          <Heading.XSmall color={theme.color.light.black} weight='regular'>
            {t('topBanner.message')}
          </Heading.XSmall>
        </S.TopBannerTextWrapper>
      )}
    </S.TopBannerContainer>
  );
};

export default TopBanner;
