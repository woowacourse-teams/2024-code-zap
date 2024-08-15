import { ReactNode, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { ToastContext } from '@/contexts';
import { useAuth } from '@/hooks/authentication';
import { useCustomContext } from '@/hooks/utils';

type RouteGuardProps = {
  children: ReactNode;
  isLoginRequired: boolean;
  redirectTo: string;
};

const RouteGuard = ({ children, isLoginRequired, redirectTo }: RouteGuardProps) => {
  const { handleLoginState, checkAlreadyLogin } = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const { infoAlert } = useCustomContext(ToastContext);
  const navigate = useNavigate();

  useEffect(() => {
    const checkLogin = async () => {
      const result = await checkAlreadyLogin();

      handleLoginState(result);
      setIsLoading(false);

      if (isLoginRequired && !result) {
        infoAlert('로그인을 해주세요.');
        navigate(redirectTo);
      }

      if (!isLoginRequired && result) {
        infoAlert('이미 로그인된 사용자입니다.');
        navigate(redirectTo);
      }
    };

    checkLogin();
  }, [checkAlreadyLogin, handleLoginState, infoAlert, isLoginRequired, navigate, redirectTo]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return children;
};

export default RouteGuard;
