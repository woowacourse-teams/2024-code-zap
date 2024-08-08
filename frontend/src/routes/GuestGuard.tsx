import { ReactNode, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

import { ToastContext } from '@/context/ToastContext';
import { useAuth } from '@/hooks/authentication/useAuth';
import useCustomContext from '@/hooks/utils/useCustomContext';

type GuestGuardProps = {
  children: ReactNode;
};

const GuestGuard = ({ children }: GuestGuardProps) => {
  const { isLogin } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);

  useEffect(() => {
    if (!isLogin) {
      infoAlert('로그인을 해주세요.');
    }
  }, [isLogin, infoAlert]);

  if (!isLogin) {
    return <Navigate to='/login' />;
  }

  return children;
};

export default GuestGuard;
