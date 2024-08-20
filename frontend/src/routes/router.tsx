import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import { TemplatePage, TemplateUploadPage, SignupPage, LoginPage, LandingPage, NotFoundPage } from '@/pages';
import HomeRouter from './HomeRouter';
import RouteGuard from './RouteGuard';

const router = createBrowserRouter([
  {
    errorElement: <NotFoundPage />,
    element: <Layout />,
    children: [
      {
        path: '/',
        element: <HomeRouter />,
      },
      {
        path: 'templates/:id',
        element: (
          <RouteGuard isLoginRequired redirectTo='/login'>
            <TemplatePage />
          </RouteGuard>
        ),
      },
      {
        path: 'templates/upload',
        element: (
          <RouteGuard isLoginRequired redirectTo='/login'>
            <TemplateUploadPage />
          </RouteGuard>
        ),
      },
      {
        path: 'signup',
        element: (
          <RouteGuard isLoginRequired={false} redirectTo='/'>
            <SignupPage />
          </RouteGuard>
        ),
      },
      {
        path: 'login',
        element: (
          <RouteGuard isLoginRequired={false} redirectTo='/'>
            <LoginPage />
          </RouteGuard>
        ),
      },
      {
        path: 'aboutus',
        element: <LandingPage />,
      },
      {
        path: '*',
        element: <NotFoundPage />,
      },
    ],
  },
]);

export default router;
