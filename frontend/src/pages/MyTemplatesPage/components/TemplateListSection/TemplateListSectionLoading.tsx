import { LoadingBall } from '@/components';

import * as S from './TemplateListSectionLoading.style';

const TemplateListSectionLoading = () => (
  <S.LoadingOverlay>
    <S.LoadingBallWrapper>
      <LoadingBall />
    </S.LoadingBallWrapper>
  </S.LoadingOverlay>
);

export default TemplateListSectionLoading;
