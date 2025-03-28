import { CodeZapLogo, HamburgerIcon, PlusIcon } from '@/assets/images';
import { Button, ContactUs, Flex, Heading, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useCustomContext, useCustomNavigate, useToggle } from '@/hooks';
import { useAuth } from '@/hooks/authentication/useAuth';
import { usePressESC } from '@/hooks/usePressESC';
import { useScrollDisable } from '@/hooks/useScrollDisable';
import { useLogoutMutation } from '@/queries/authentication/useLogoutMutation';
import { ROUTE_END_POINT } from '@/routes/endPoints';
import { trackClickNewTemplate } from '@/service/amplitude';
import { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';

import { theme } from '@design/style/theme';

import * as S from './Header.style';

const Header = ({
  headerRef,
}: {
  headerRef: React.RefObject<HTMLDivElement>;
}) => {
  const {
    isLogin,
    isChecking,
    memberInfo: { memberId },
  } = useAuth();
  const [isMenuOpen, toggleMenu] = useToggle();
  const { failAlert } = useCustomContext(ToastContext);
  const location = useLocation();
  const navigate = useCustomNavigate();
  const { t } = useTranslation('Header');

  useScrollDisable(isMenuOpen);
  usePressESC(isMenuOpen, toggleMenu);

  useEffect(() => {
    if (isMenuOpen) {
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
    trackClickNewTemplate();

    if (!isLogin) {
      failAlert(t('alertLogin'));

      return;
    }

    navigate(ROUTE_END_POINT.TEMPLATES_UPLOAD);
  };

  return (
    <S.HeaderContainer ref={headerRef}>
      <S.HeaderContentContainer>
        <Logo />
        <S.HeaderMenu menuOpen={isMenuOpen}>
          <S.NavContainer>
            {!isChecking && isLogin && memberId && (
              <>
                <NavOption
                  route={ROUTE_END_POINT.memberTemplates(memberId)}
                  name={t('myTemplates')}
                />
                <NavOption
                  route={ROUTE_END_POINT.memberLikedTemplates(memberId)}
                  name={t('likedTemplates')}
                />
              </>
            )}
            <NavOption
              route={ROUTE_END_POINT.TEMPLATES_EXPLORE}
              name={t('explore')}
            />

            <ContactUs />
          </S.NavContainer>
          <S.NavContainer>
            <S.MobileHiddenButton
              variant='outlined'
              size='medium'
              weight='bold'
              hoverStyle='none'
              onClick={handleTemplateUploadButton}
              aria-description={t('newTemplateAria')}
            >
              <PlusIcon />
              {t('newTemplate')}
            </S.MobileHiddenButton>

            {!isChecking && isLogin ? <LogoutButton /> : <LoginButton />}
          </S.NavContainer>
        </S.HeaderMenu>
        <S.MobileMenuContainer>
          <Button
            variant='outlined'
            size='small'
            weight='bold'
            hoverStyle='none'
            onClick={handleTemplateUploadButton}
            aria-description={t('newTemplateAria')}
          >
            <PlusIcon />
            {t('newTemplate')}
          </Button>
          <HeaderMenuButton menuOpen={isMenuOpen} toggleMenu={toggleMenu} />
        </S.MobileMenuContainer>
      </S.HeaderContentContainer>
      {isMenuOpen && <S.Dimmed menuOpen={isMenuOpen} onClick={toggleMenu} />}
    </S.HeaderContainer>
  );
};

const Logo = () => {
  const location = useLocation();
  const isLandingPage = location.pathname === '/';
  const { t } = useTranslation('Header');

  return (
    <Link to={ROUTE_END_POINT.HOME}>
      <Flex align='center' gap='0.5rem'>
        <CodeZapLogo aria-label={t('logoButton')} />
        <Heading.XSmall
          color={
            isLandingPage
              ? theme.color.light.primary_500
              : theme.color.light.secondary_800
          }
        >
          코드잽
        </Heading.XSmall>
      </Flex>
    </Link>
  );
};

const NavOption = ({ route, name }: { route: string; name: string }) => {
  const location = useLocation();
  const isCurrentPage = location.pathname === route;

  return (
    <Link to={route}>
      <S.NavOptionButton>
        <Text.Medium
          weight='bold'
          color={
            isCurrentPage
              ? theme.color.light.primary_500
              : theme.color.light.secondary_800
          }
        >
          {name}
        </Text.Medium>
      </S.NavOptionButton>
    </Link>
  );
};

const LogoutButton = () => {
  const { mutateAsync } = useLogoutMutation();
  const { t } = useTranslation('Header');

  const handleLogoutButton = async () => {
    await mutateAsync();
  };

  return (
    <Button
      variant='text'
      size='medium'
      weight='bold'
      hoverStyle='none'
      onClick={handleLogoutButton}
    >
      {t('logout')}
    </Button>
  );
};

const LoginButton = () => {
  const { t } = useTranslation('Header');

  return (
    <Link to={ROUTE_END_POINT.LOGIN}>
      <Button variant='text' size='medium' weight='bold' hoverStyle='none'>
        {t('login')}
      </Button>
    </Link>
  );
};

const HeaderMenuButton = ({
  menuOpen,
  toggleMenu,
}: {
  menuOpen: boolean;
  toggleMenu: () => void;
}) => {
  const { t } = useTranslation('Header');

  return (
    <S.HamburgerIconWrapper aria-label={t('menu')}>
      <HamburgerIcon menuOpen={menuOpen} onClick={toggleMenu} />
    </S.HamburgerIconWrapper>
  );
};

export default Header;
