import { ReactElement, useEffect } from 'react';

import { ToastContext } from '@/contexts';
import { useCustomContext, useCustomNavigate } from '@/hooks';
import { useAuth } from '@/hooks/authentication';

type RouteGuardProps = {
  children: ReactElement;
  isLoginRequired: boolean;
  redirectTo: string;
};

const RouteGuard = ({ children, isLoginRequired, redirectTo }: RouteGuardProps) => {
  const { isLogin, isChecking } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);
  const navigate = useCustomNavigate();

  useEffect(() => {
    if (isLoginRequired && !isChecking && !isLogin) {
      infoAlert('로그인을 해주세요.');
      navigate(redirectTo);
    }

    if (!isLoginRequired && !isChecking && isLogin) {
      infoAlert('이미 로그인된 사용자입니다.');
      navigate(redirectTo);
    }
  }, [isLogin, isChecking, infoAlert, isLoginRequired, navigate, redirectTo]);

  if (isChecking) {
    return null;
  }

  return children;
};

export default RouteGuard;
