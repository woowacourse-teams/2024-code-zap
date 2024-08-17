import { Link } from 'react-router-dom';

import { codezapLogo, newTemplateIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useLogoutMutation } from '@/queries/authentication/useLogoutMutation';
import { theme } from '../../style/theme';
import * as S from './Header.style';

const Header = ({ headerRef }: { headerRef: React.RefObject<HTMLDivElement> }) => {
  const { isLogin, isChecking } = useAuth();

  if (isChecking) {
    return null;
  }

  return (
    <S.HeaderContainer ref={headerRef}>
      <S.HeaderContentContainer>
        <Logo />
        <Flex align='center' gap='2rem' flex='1'>
          {!isChecking && isLogin && <NavOption route='/' name='내 템플릿' />}
          <NavOption route='/aboutus' name='서비스 소개' />
        </Flex>

        <Flex align='center' gap='2rem'>
          <Link to={'/templates/upload'}>
            <Button variant='outlined' size='medium' weight='bold' hoverStyle='none'>
              <img src={newTemplateIcon} alt='' width={12} height={12} />새 템플릿
            </Button>
          </Link>
          {!isChecking && isLogin ? <LogoutButton /> : <LoginButton />}
        </Flex>
      </S.HeaderContentContainer>
    </S.HeaderContainer>
  );
};

const Logo = () => (
  <Link to={'/'}>
    <Flex align='center' gap='0.5rem'>
      <img src={codezapLogo} alt='로고 버튼' width={36} height={18} />
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

export default Header;
