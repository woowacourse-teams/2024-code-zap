import { lazy } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import RouteGuard from '@/routes/RouteGuard';
import { ROUTE_END_POINT } from '@/routes/endPoints';

/* eslint-disable react-refresh/only-export-components */
const LandingPage = lazy(() => import('@/pages/LandingPage/LandingPage'));
const TemplatePage = lazy(() => import('@/pages/TemplatePage/TemplatePage'));
const TemplateUploadPage = lazy(() => import('@/pages/TemplateUploadPage/TemplateUploadPage'));
const SignupPage = lazy(() => import('@/pages/SignupPage/SignupPage'));
const LoginPage = lazy(() => import('@/pages/LoginPage/LoginPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage/NotFoundPage'));
const TemplateExplorePage = lazy(() => import('@/pages/TemplateExplorePage/TemplateExplorePage'));
const MyTemplatePage = lazy(() => import('@/pages/MyTemplatesPage/MyTemplatePage'));
const MyLikedTemplatePage = lazy(() => import('@/pages/MyLikedTemplatePage/MyLikedTemplatePage'));

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      {
        path: ROUTE_END_POINT.HOME,
        element: <LandingPage />,
      },
      {
        path: ROUTE_END_POINT.MEMBERS_TEMPLATES,
        element: <MyTemplatePage />,
      },
      {
        path: ROUTE_END_POINT.MEMBERS_LIKED_TEMPLATES,
        element: (
          <RouteGuard isLoginRequired redirectTo={ROUTE_END_POINT.LOGIN}>
            <MyLikedTemplatePage />
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.TEMPLATES_EXPLORE,
        element: <TemplateExplorePage />,
      },
      {
        path: ROUTE_END_POINT.TEMPLATE,
        element: <TemplatePage />,
      },
      {
        path: ROUTE_END_POINT.TEMPLATES_UPLOAD,
        element: (
          <RouteGuard isLoginRequired redirectTo={ROUTE_END_POINT.LOGIN}>
            <TemplateUploadPage />
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.SIGNUP,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={ROUTE_END_POINT.HOME}>
            <SignupPage />
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.LOGIN,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={ROUTE_END_POINT.HOME}>
            <LoginPage />
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
