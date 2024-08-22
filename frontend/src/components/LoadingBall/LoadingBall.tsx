import * as S from './LoadingBall.style';

const LoadingBall = () => (
  <S.LoadingBallContainer>
    <S.LoadingBallWrapper>
      <S.Ball />
      <S.Ball />
      <S.Ball />
    </S.LoadingBallWrapper>
  </S.LoadingBallContainer>
);

export default LoadingBall;
