import { ErrorBoundary } from '@sentry/react';
import { lazy, Suspense } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import { Layout, LoadingFallback } from '@/components';

import RouteGuard from './RouteGuard';
import { END_POINTS } from './endPoints';

const LandingPage = lazy(() => import('@/pages/LandingPage/LandingPage'));
const TemplatePage = lazy(() => import('@/pages/TemplatePage/TemplatePage'));
const TemplateUploadPage = lazy(() => import('@/pages/TemplateUploadPage/TemplateUploadPage'));
const SignupPage = lazy(() => import('@/pages/SignupPage/SignupPage'));
const LoginPage = lazy(() => import('@/pages/LoginPage/LoginPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage/NotFoundPage'));
const TemplateExplorePage = lazy(() => import('@/pages/TemplateExplorePage/TemplateExplorePage'));
const MyTemplatePage = lazy(() => import('@/pages/MyTemplatesPage/MyTemplatePage'));

const CustomSuspense = ({ children }: { children: JSX.Element }) => (
  <Suspense fallback={<div>Loading...</div>}>{children}</Suspense>
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
        path: END_POINTS.HOME,
        element: (
          <CustomSuspense>
            <LandingPage />
          </CustomSuspense>
        ),
      },
      {
        path: END_POINTS.MEMBER_TEMPLATES,
        element: (
          <RouteGuard isLoginRequired redirectTo={END_POINTS.LOGIN}>
            <ErrorBoundary fallback={<NotFoundPage />}>
              <Suspense fallback={<LoadingFallback />}>
                <MyTemplatePage />
              </Suspense>
            </ErrorBoundary>
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.TEMPLATES_EXPLORE,
        element: (
          <CustomSuspense>
            <TemplateExplorePage />
          </CustomSuspense>
        ),
      },
      {
        path: END_POINTS.TEMPLATE,
        element: (
          <CustomSuspense>
            <TemplatePage />
          </CustomSuspense>
        ),
      },
      {
        path: END_POINTS.TEMPLATES_UPLOAD,
        element: (
          <RouteGuard isLoginRequired redirectTo={END_POINTS.LOGIN}>
            <CustomSuspense>
              <TemplateUploadPage />
            </CustomSuspense>
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.SIGNUP,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={END_POINTS.HOME}>
            <CustomSuspense>
              <SignupPage />
            </CustomSuspense>
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.LOGIN,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={END_POINTS.HOME}>
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
