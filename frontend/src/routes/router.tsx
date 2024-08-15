import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import {
  TemplatePage,
  MyTemplatePage,
  TemplateUploadPage,
  SignupPage,
  LoginPage,
  LandingPage,
  NotFoundPage,
} from '@/pages';
import RouteGuard from './RouteGuard';

const router = createBrowserRouter([
  {
    errorElement: <NotFoundPage />,
    element: <Layout />,
    children: [
      {
        path: '/',
        element: (
          <RouteGuard isLoginRequired redirectTo='/login'>
            <MyTemplatePage />
          </RouteGuard>
        ),
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
        path: 'home',
        element: (
          <RouteGuard isLoginRequired={false} redirectTo='/'>
            <LandingPage />,
          </RouteGuard>
        ),
      },
      {
        path: '*',
        element: <NotFoundPage />,
      },
    ],
  },
]);

export default router;
