import { ReactNode, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

import { ToastContext } from '@/context/ToastContext';
import { useAuth } from '@/hooks/authentication/useAuth';
import useCustomContext from '@/hooks/utils/useCustomContext';

type AuthGuardProps = {
  children: ReactNode;
};

const AuthGuard = ({ children }: AuthGuardProps) => {
  const { isLogin } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);

  useEffect(() => {
    if (isLogin) {
      infoAlert('이미 로그인된 사용자입니다.');
    }
  }, [isLogin, infoAlert]);

  if (isLogin) {
    return <Navigate to='/' />;
  }

  return children;
};

export default AuthGuard;
