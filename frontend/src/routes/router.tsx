import { Outlet, createBrowserRouter } from 'react-router-dom';

import Template from '@/pages/Template';
import TemplateList from '@/pages/TemplateList';
import UploadsTemplate from '@/pages/UploadsTemplate';

const Layout = () => {
  const style = { maxWidth: '1024px', margin: 'auto', padding: '0 2rem' };

  return (
    <div css={style}>
      <Outlet />
    </div>
  );
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
      {
        path: 'templates/uploads',
        element: <UploadsTemplate />,
      },
    ],
  },
]);

export default router;
