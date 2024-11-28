import { PropsWithChildren } from 'react';

import { ZapzapCuriousLogo } from '@/assets/images';
import { Text } from '@/components';
import { theme } from '@/style/theme';

import * as S from './NoResults.style';

const NoResults = ({ children }: PropsWithChildren) => (
  <S.NoResultsContainer>
    <ZapzapCuriousLogo width={48} height={48} />
    <Text.Large color={theme.color.light.secondary_700}>{children}</Text.Large>
  </S.NoResultsContainer>
);

export default NoResults;
