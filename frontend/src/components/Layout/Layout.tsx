import { Outlet } from 'react-router-dom';
import { Header } from '../Header';

const Layout = () => {
  const style = { maxWidth: '1024px', margin: 'auto', padding: '0 2rem' };

  return (
    <div css={style}>
      <Header />
      <Outlet />
    </div>
  );
};

export default Layout;
