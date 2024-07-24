import { Outlet } from 'react-router-dom';
import { Header } from '..';
import * as S from './style';

const Layout = () => (
  <S.LayoutContainer>
    <Header />
    <Outlet />
  </S.LayoutContainer>
);

export default Layout;
