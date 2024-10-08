import { useNavigate, useLocation, NavigateOptions, To } from 'react-router-dom';
/**
 * useCustomNavigate - 현재 위치와 대상 위치를 비교하여 불필요한 네비게이션을 방지합니다.
 *
 * @returns {Function} 커스텀 네비게이트 함수
 *   @param {To | number} to - 네비게이션 대상. 문자열 경로, 위치 객체 또는 히스토리 스택의 상대적 위치를 나타내는 숫자일 수 있습니다.
 *   @param {NavigateOptions} [options] - 네비게이션 옵션 (선택적)
 *
 */
export const useCustomNavigate = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (to: To | number, options?: NavigateOptions) => {
    if (typeof to === 'number') {
      navigate(to);

      return;
    }

    if (typeof to === 'string' && to !== location.pathname) {
      navigate(to, options);

      return;
    }

    if (typeof to === 'object' && to.pathname !== location.pathname) {
      navigate(to, options);
    }
  };
};
