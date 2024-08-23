import { useAuth } from '@/hooks/authentication';
import { LandingPage, MyTemplatePage } from '@/pages';

const HomeRouter = () => {
  const { isLogin, isChecking } = useAuth();

  if (isChecking) {
    return null;
  }

  if (isLogin) {
    return <MyTemplatePage />;
  }

  return <LandingPage />;
};

export default HomeRouter;
