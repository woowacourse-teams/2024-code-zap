import { Outlet, createBrowserRouter } from 'react-router-dom';

import Templates from '@/pages/Templates';

const Layout = () => {
  return <Outlet />;
};

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      {
        path: '/',
        element: <Templates />,
      },
    ],
  },
]);

export default router;
