import { ReactNode, useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

import { useAuth } from '@/hooks/authentication';

type RouteGuardProps = {
  children: ReactNode;
  isLoginRequired: boolean;
  redirectTo: string;
};

const RouteGuard = ({ children, isLoginRequired, redirectTo }: RouteGuardProps) => {
  const { checkAlreadyLogin } = useAuth();
  const [isChecking, setIsChecking] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean | null>(null);

  useEffect(() => {
    const checkLogin = async () => {
      const result = await checkAlreadyLogin();

      setIsLoggedIn(result);
      setIsChecking(false);
    };

    checkLogin();
  }, []);

  if (isChecking) {
    return <div>Loading...</div>;
  }

  if (isLoginRequired && !isLoggedIn) {
    console.log('로그인되지 않은 사용자입니다.');

    return <Navigate to={redirectTo} />;
  }

  if (!isLoginRequired && isLoggedIn) {
    console.log('이미 로그인된 사용자입니다.');

    return <Navigate to={redirectTo} />;
  }

  return children;
};

export default RouteGuard;
