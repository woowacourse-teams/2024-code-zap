import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components';
import { Template, TemplateList, TemplateUpload } from '@/pages';

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      {
        path: '/',
        element: <TemplateList />,
      },
      {
        path: 'templates/:id',
        element: <Template />,
      },
      {
        path: 'templates/uploads',
        element: <TemplateUpload />,
      },
    ],
  },
]);

export default router;
