import { Outlet, createBrowserRouter } from 'react-router-dom';

import Template from '@/pages/Template';
import TemplateList from '@/pages/TemplateList';

const Layout = () => {
  return <Outlet />;
};

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
    ],
  },
]);

export default router;
