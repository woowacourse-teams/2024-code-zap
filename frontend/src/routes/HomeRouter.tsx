import { useAuth } from '@/hooks/authentication';
import { LandingPage, MyTemplatePage } from '@/pages';

const HomeRouter = () => {
  const { isLogin } = useAuth();

  if (isLogin) {
    return <MyTemplatePage />;
  }

  return <LandingPage />;
};

export default HomeRouter;
