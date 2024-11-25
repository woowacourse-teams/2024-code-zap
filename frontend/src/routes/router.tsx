import { ErrorBoundary } from '@sentry/react';
import { lazy, Suspense } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import { Layout, LoadingBall } from '@/components';
import RouteGuard from '@/routes/RouteGuard';
import { ROUTE_END_POINT } from '@/routes/endPoints';

const LandingPage = lazy(() => import('@/pages/LandingPage/LandingPage'));
const TemplatePage = lazy(() => import('@/pages/TemplatePage/TemplatePage'));
const TemplateUploadPage = lazy(() => import('@/pages/TemplateUploadPage/TemplateUploadPage'));
const SignupPage = lazy(() => import('@/pages/SignupPage/SignupPage'));
const LoginPage = lazy(() => import('@/pages/LoginPage/LoginPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage/NotFoundPage'));
const TemplateExplorePage = lazy(() => import('@/pages/TemplateExplorePage/TemplateExplorePage'));
const MyTemplatePage = lazy(() => import('@/pages/MyTemplatesPage/MyTemplatePage'));
const MyLikedTemplatePage = lazy(() => import('@/pages/MyLikedTemplatePage/MyLikedTemplatePage'));

const CustomSuspense = ({ children }: { children: JSX.Element }) => (
  <Suspense
    fallback={
      <div style={{ height: '100vh' }}>
        <LoadingBall />
      </div>
    }
  >
    {children}
  </Suspense>
);

const router = createBrowserRouter([
  {
    errorElement: (
      <CustomSuspense>
        <NotFoundPage />
      </CustomSuspense>
    ),
    element: (
      <CustomSuspense>
        <Layout />
      </CustomSuspense>
    ),
    children: [
      {
        path: ROUTE_END_POINT.HOME,
        element: (
          <CustomSuspense>
            <LandingPage />
          </CustomSuspense>
        ),
      },
      {
        path: ROUTE_END_POINT.MEMBERS_TEMPLATES,
        element: (
          <ErrorBoundary fallback={<NotFoundPage />}>
            <MyTemplatePage />
          </ErrorBoundary>
        ),
      },
      {
        path: ROUTE_END_POINT.MEMBERS_LIKED_TEMPLATES,
        element: (
          <RouteGuard isLoginRequired redirectTo={ROUTE_END_POINT.LOGIN}>
            <ErrorBoundary fallback={<NotFoundPage />}>
              <MyLikedTemplatePage />
            </ErrorBoundary>
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.TEMPLATES_EXPLORE,
        element: (
          <CustomSuspense>
            <TemplateExplorePage />
          </CustomSuspense>
        ),
      },
      {
        path: ROUTE_END_POINT.TEMPLATE,
        element: (
          <CustomSuspense>
            <TemplatePage />
          </CustomSuspense>
        ),
      },
      {
        path: ROUTE_END_POINT.TEMPLATES_UPLOAD,
        element: (
          <RouteGuard isLoginRequired redirectTo={ROUTE_END_POINT.LOGIN}>
            <CustomSuspense>
              <TemplateUploadPage />
            </CustomSuspense>
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.SIGNUP,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={ROUTE_END_POINT.HOME}>
            <CustomSuspense>
              <SignupPage />
            </CustomSuspense>
          </RouteGuard>
        ),
      },
      {
        path: ROUTE_END_POINT.LOGIN,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={ROUTE_END_POINT.HOME}>
            <CustomSuspense>
              <LoginPage />
            </CustomSuspense>
          </RouteGuard>
        ),
      },
      {
        path: '*',
        element: (
          <CustomSuspense>
            <NotFoundPage />
          </CustomSuspense>
        ),
      },
    ],
  },
]);

export default router;
