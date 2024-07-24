import { createBrowserRouter } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import Template from '@/pages/Template/Template';
import TemplateList from '@/pages/TemplateList';
import TemplateUpload from '@/pages/TemplateUpload';

const router = createBrowserRouter(
  [
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
  ],
  { basename: process.env.NODE_ENV === 'development' ? '/' : process.env.REACT_APP_BASE_URL },
);

export default router;
