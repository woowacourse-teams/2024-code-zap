import { createBrowserRouter } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import Template from '@/pages/Template';
import TemplateList from '@/pages/TemplateList';
import UploadsTemplate from '@/pages/UploadsTemplate';

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
        element: <UploadsTemplate />,
      },
    ],
  },
]);

export default router;
