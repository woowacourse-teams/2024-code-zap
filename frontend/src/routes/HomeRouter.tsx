import { useEffect, useState } from 'react';

import { useAuth } from '@/hooks/authentication';
import { LandingPage, MyTemplatePage } from '@/pages';

const HomeRouter = () => {
  const { isLogin, handleLoginState, checkAlreadyLogin } = useAuth();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkLogin = async () => {
      const result = await checkAlreadyLogin();

      handleLoginState(result);
      setIsLoading(false);
    };

    checkLogin();
  }, [checkAlreadyLogin, handleLoginState]);

  if (isLoading) {
    return null;
  }

  return isLogin ? <MyTemplatePage /> : <LandingPage />;
};

export default HomeRouter;
