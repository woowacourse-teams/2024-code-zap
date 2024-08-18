import { Link } from 'react-router-dom';

import { CodeZapLogo, HamburgerIcon, PlusIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useToggle } from '@/hooks/utils';
import { useScrollDisable } from '@/hooks/utils/useScrollDisable';
import { useLogoutMutation } from '@/queries/authentication/useLogoutMutation';
import { theme } from '../../style/theme';
import * as S from './Header.style';

const Header = ({ headerRef }: { headerRef: React.RefObject<HTMLDivElement> }) => {
  const { isLogin } = useAuth();
  const [menuOpen, toggleMenu] = useToggle();

  useScrollDisable(menuOpen);

  return (
    <S.HeaderContainer ref={headerRef}>
      <S.HeaderContentContainer>
        <Logo />
        <S.HeaderMenu menuOpen={menuOpen}>
          <Flex align='center' gap='2rem'>
            {isLogin && <NavOption route='/' name='내 템플릿' />}
            <NavOption route='/aboutus' name='서비스 소개' />
          </Flex>

          <Flex align='center' gap='2rem'>
            <Link to={'/templates/upload'}>
              <Button variant='outlined' size='medium' weight='bold' hoverStyle='none'>
                <PlusIcon aria-label='' />새 템플릿
              </Button>
            </Link>
            {isLogin ? <LogoutButton /> : <LoginButton />}
          </Flex>
        </S.HeaderMenu>
        <HeaderMenuButton menuOpen={menuOpen} toggleMenu={toggleMenu} />
      </S.HeaderContentContainer>
    </S.HeaderContainer>
  );
};

const Logo = () => (
  <Link to={'/'}>
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
  <Link to='/login'>
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
