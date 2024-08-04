import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import { MyTemplatePage, TemplatePage, TemplateUploadPage } from '@/pages';

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      {
        path: '/',
        element: <MyTemplatePage />,
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
