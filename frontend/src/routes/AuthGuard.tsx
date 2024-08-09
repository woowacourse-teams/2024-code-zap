import { ReactNode, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

import { ToastContext } from '@/context/ToastContext';
import { useAuth } from '@/hooks/authentication/useAuth';
import useCustomContext from '@/hooks/utils/useCustomContext';
import { useLoginStateQuery } from '@/queries/authentication';

type AuthGuardProps = {
  children: ReactNode;
};

const AuthGuard = ({ children }: AuthGuardProps) => {
  const { isLogin, handleLoginState } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);

  const { isError } = useLoginStateQuery();

  useEffect(() => {
    if (isError) {
      localStorage.removeItem('username');
      localStorage.removeItem('memberId');
      handleLoginState(false);
    }
  }, [isError]);

  useEffect(() => {
    if (isLogin) {
      const savedUsername = localStorage.getItem('username');
      const savedMemberId = localStorage.getItem('memberId');

      if (savedMemberId && savedUsername) {
        infoAlert('이미 로그인된 사용자입니다.');
      }
    }
  }, [isLogin, infoAlert]);

  if (isLogin) {
    return <Navigate to='/' />;
  }

  return children;
};

export default AuthGuard;
