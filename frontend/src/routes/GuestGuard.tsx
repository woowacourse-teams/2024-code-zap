import { ReactNode, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

import { ToastContext } from '@/contexts';
import { useAuth } from '@/hooks/authentication';
import { useCustomContext } from '@/hooks/utils';
import { useLoginStateQuery } from '@/queries/authentication';

type GuestGuardProps = {
  children: ReactNode;
};

const GuestGuard = ({ children }: GuestGuardProps) => {
  const { isLogin, handleLoginState, memberInfo } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);

  const { isSuccess } = useLoginStateQuery();

  useEffect(() => {
    if (isSuccess) {
      localStorage.setItem('username', String(memberInfo.username));
      localStorage.setItem('memberId', String(memberInfo.memberId));
      handleLoginState(true);
    }
  }, [isSuccess]);

  useEffect(() => {
    if (!isLogin) {
      const savedUsername = localStorage.getItem('username');
      const savedMemberId = localStorage.getItem('memberId');

      if (savedMemberId === undefined && savedUsername === undefined) {
        infoAlert('로그인을 해주세요.');
      }
    }
  }, [isLogin, infoAlert]);

  if (!isLogin) {
    return <Navigate to='/login' />;
  }

  return children;
};

export default GuestGuard;
