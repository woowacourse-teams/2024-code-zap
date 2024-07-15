import { Outlet, createBrowserRouter } from 'react-router-dom';

import Template from '@/pages/Template';
import TemplateList from '@/pages/TemplateList';
import Header from '../components/Header/Header';

const Layout = () => {
  const style = { maxWidth: '1024px', margin: 'auto', padding: '0 2rem' };

  return (
    <div css={style}>
      <Header />
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
    ],
  },
]);

export default router;
