import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import {
  TemplatePage,
  TemplateUploadPage,
  SignupPage,
  LoginPage,
  LandingPage,
  NotFoundPage,
  TemplateExplorePage,
  MyTemplatePage,
} from '@/pages';
import RouteGuard from './RouteGuard';
import { END_POINTS } from './endPoints';

const router = createBrowserRouter([
  {
    errorElement: <NotFoundPage />,
    element: <Layout />,
    children: [
      {
        path: END_POINTS.home,
        element: <LandingPage />,
      },
      {
        path: END_POINTS.myTemplates,
        element: (
          <RouteGuard isLoginRequired redirectTo={END_POINTS.login}>
            <MyTemplatePage />
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.templatesExplore,
        element: <TemplateExplorePage />,
      },
      {
        path: END_POINTS.template,
        element: <TemplatePage />,
      },
      {
        path: END_POINTS.templatesUpload,
        element: (
          <RouteGuard isLoginRequired redirectTo={END_POINTS.login}>
            <TemplateUploadPage />
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.signup,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={END_POINTS.home}>
            <SignupPage />
          </RouteGuard>
        ),
      },
      {
        path: END_POINTS.login,
        element: (
          <RouteGuard isLoginRequired={false} redirectTo={END_POINTS.home}>
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
