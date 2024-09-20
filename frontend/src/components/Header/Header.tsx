import { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

import { CodeZapLogo, HamburgerIcon, PlusIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useCustomContext, useToggle } from '@/hooks';
import { useAuth } from '@/hooks/authentication/useAuth';
import { usePressESC } from '@/hooks/usePressESC';
import { useScrollDisable } from '@/hooks/useScrollDisable';
import { useLogoutMutation } from '@/queries/authentication/useLogoutMutation';
import { END_POINTS } from '@/routes';

import { theme } from '../../style/theme';
import * as S from './Header.style';

const Header = ({ headerRef }: { headerRef: React.RefObject<HTMLDivElement> }) => {
  const { isLogin, isChecking } = useAuth();
  const [menuOpen, toggleMenu] = useToggle();
  const { failAlert } = useCustomContext(ToastContext);
  const location = useLocation();
  const navigate = useNavigate();

  useScrollDisable(menuOpen);
  usePressESC(menuOpen, toggleMenu);

  useEffect(() => {
    if (menuOpen) {
      toggleMenu();
    }
  }, [location.pathname]);

  if (isChecking) {
    return (
      <S.HeaderContainer ref={headerRef}>
        <S.HeaderContentContainer></S.HeaderContentContainer>
      </S.HeaderContainer>
    );
  }

  const handleTemplateUploadButton = () => {
    if (!isLogin) {
      failAlert('로그인을 해주세요.');

      return;
    }

    navigate(END_POINTS.TEMPLATES_UPLOAD);
  };

  return (
    <S.HeaderContainer ref={headerRef}>
      <S.HeaderContentContainer>
        <Logo />
        <S.HeaderMenu menuOpen={menuOpen}>
          <S.NavContainer>
            {!isChecking && isLogin && <NavOption route={END_POINTS.MY_TEMPLATES} name='내 템플릿' />}
            <NavOption route={END_POINTS.TEMPLATES_EXPLORE} name='구경가기' />
          </S.NavContainer>
          <S.NavContainer>
            <S.MobileHiddenButton
              variant='outlined'
              size='medium'
              weight='bold'
              hoverStyle='none'
              onClick={handleTemplateUploadButton}
            >
              <PlusIcon aria-label='' />새 템플릿
            </S.MobileHiddenButton>

            {!isChecking && isLogin ? <LogoutButton /> : <LoginButton />}
          </S.NavContainer>
        </S.HeaderMenu>
        <S.MobileMenuContainer>
          <Button variant='outlined' size='small' weight='bold' hoverStyle='none' onClick={handleTemplateUploadButton}>
            <PlusIcon aria-label='' />새 템플릿
          </Button>
          <HeaderMenuButton menuOpen={menuOpen} toggleMenu={toggleMenu} />
        </S.MobileMenuContainer>
      </S.HeaderContentContainer>
      {menuOpen && <S.Dimmed menuOpen={menuOpen} onClick={toggleMenu} />}
    </S.HeaderContainer>
  );
};

const Logo = () => (
  <Link to={END_POINTS.HOME}>
    <Flex align='center' gap='0.5rem'>
      <CodeZapLogo aria-label='로고 버튼' />
      <Heading.XSmall color={theme.color.light.primary_500}>코드잽</Heading.XSmall>
    </Flex>
  </Link>
);

const NavOption = ({ route, name }: { route: string; name: string }) => (
  <Link to={route}>
    <S.NavOptionButton>
      <Text.Medium weight='bold' color={theme.color.light.secondary_800}>
        {name}
      </Text.Medium>
    </S.NavOptionButton>
  </Link>
);

const LogoutButton = () => {
  const { mutateAsync } = useLogoutMutation();

  const handleLogoutButton = async () => {
    await mutateAsync();
  };

  return (
    <Button variant='text' size='medium' weight='bold' hoverStyle='none' onClick={handleLogoutButton}>
      로그아웃
    </Button>
  );
};

const LoginButton = () => (
  <Link to={END_POINTS.LOGIN}>
    <Button variant='text' size='medium' weight='bold' hoverStyle='none'>
      로그인
    </Button>
  </Link>
);

const HeaderMenuButton = ({ menuOpen, toggleMenu }: { menuOpen: boolean; toggleMenu: () => void }) => (
  <S.HamburgerIconWrapper>
    <HamburgerIcon menuOpen={menuOpen} onClick={toggleMenu} />
  </S.HamburgerIconWrapper>
);

export default Header;
