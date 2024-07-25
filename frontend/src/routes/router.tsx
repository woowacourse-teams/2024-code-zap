import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import { TemplatePage, TemplateListPage, TemplateUploadPage } from '@/pages';

const router = createBrowserRouter([
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
    ],
  },
]);

export default router;
