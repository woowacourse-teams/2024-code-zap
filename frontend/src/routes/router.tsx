import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import { TemplatePage, TemplateListPage, TemplateUploadPage, TemplateEditPage } from '@/pages';

const router = createBrowserRouter(
  [
    {
      element: <Layout />,
      children: [
        {
          path: '/',
          element: <TemplateListPage />,
        },
        {
          path: 'templates/:id',
          element: <TemplatePage />,
        },
        {
          path: 'templates/upload',
          element: <TemplateUploadPage />,
        },
        {
          path: 'templates/edit/:id',
          element: <TemplateEditPage />,
        },
      ],
    },
  ],
  { basename: process.env.NODE_ENV === 'development' ? '/' : process.env.REACT_APP_BASE_URL },
);

export default router;
