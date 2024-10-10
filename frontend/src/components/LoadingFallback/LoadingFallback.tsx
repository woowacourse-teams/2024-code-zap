import LoadingBall from '../LoadingBall/LoadingBall';
import * as S from './LoadingFallback.style';

const LoadingFallback = () => (
  <S.FallbackContainer>
    <LoadingBall />
  </S.FallbackContainer>
);

export default LoadingFallback;
