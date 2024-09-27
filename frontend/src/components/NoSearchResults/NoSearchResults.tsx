import { ZapzapCuriousLogo } from '@/assets/images';
import { theme } from '@/style/theme';

import { Text } from '../';
import * as S from './NoSearchResults.style';

const NoSearchResults = () => (
  <S.NoSearchResultsContainer>
    <ZapzapCuriousLogo width={48} height={48} />
    <Text.Large color={theme.color.light.secondary_700}>검색 결과가 없습니다.</Text.Large>
  </S.NoSearchResultsContainer>
);

export default NoSearchResults;
