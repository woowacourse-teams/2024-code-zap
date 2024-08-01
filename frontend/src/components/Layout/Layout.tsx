import { Outlet } from 'react-router-dom';

import { Header } from '@/components';
import * as S from './Layout.style';

const Layout = () => (
  <S.LayoutContainer>
    <Header />
    <S.Wrapper>
      <Outlet />
    </S.Wrapper>
  </S.LayoutContainer>
);

export default Layout;
