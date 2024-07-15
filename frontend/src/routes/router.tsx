import { Outlet, createBrowserRouter } from 'react-router-dom';

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
    ],
  },
]);

export default router;
