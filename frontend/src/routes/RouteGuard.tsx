import { ReactNode, useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

import { useAuth } from '@/hooks/authentication';

type RouteGuardProps = {
  children: ReactNode;
  isLoginRequired: boolean;
  redirectTo: string;
};

const RouteGuard = ({ children, isLoginRequired, redirectTo }: RouteGuardProps) => {
  const { isLogin, handleLoginState, checkAlreadyLogin } = useAuth();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkLogin = async () => {
      const result = await checkAlreadyLogin();

      handleLoginState(result);
      setIsLoading(false);
    };

    checkLogin();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isLoginRequired && !isLogin) {
    console.log('로그인되지 않은 사용자입니다.');

    return <Navigate to={redirectTo} />;
  }

  if (!isLoginRequired && isLogin) {
    console.log('이미 로그인된 사용자입니다.');

    return <Navigate to={redirectTo} />;
  }

  return children;
};

export default RouteGuard;
