import { Link } from 'react-router-dom';

import { logoIcon, newTemplateIcon, userMenuIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { useCheckLoginState } from '@/hooks/authentication';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useLogoutMutation } from '@/queries/authentication/useLogoutMutation';
import { theme } from '../../style/theme';
import * as S from './Header.style';

const Header = () => {
  const { isLogin } = useAuth();

  useCheckLoginState();

  return (
    <S.HeaderContainer>
      <S.HeaderContentContainer>
        <Logo />
        <Flex align='center' gap='2rem' flex='1'>
          <NavOption route='/' name='내 템플릿' />
          <NavOption route='/explore' name='구경가기' />
        </Flex>

        <Flex align='center' gap='2rem'>
          <Link to={'/templates/upload'}>
            <Button variant='outlined' size='medium' weight='bold' hoverStyle='none'>
              <img src={newTemplateIcon} alt='' />새 템플릿
            </Button>
          </Link>
          {isLogin ? <UserMenuButton /> : <LoginButton />}
        </Flex>
      </S.HeaderContentContainer>
    </S.HeaderContainer>
  );
};

const Logo = () => (
  <Link to={'/'}>
    <Flex align='center' gap='1rem'>
      <img src={logoIcon} alt='로고 버튼' />
      <Heading.XSmall color={theme.color.light.primary_800}>코드잽</Heading.XSmall>
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

const UserMenuButton = () => {
  const { mutateAsync } = useLogoutMutation();

  const handleLogoutButton = async () => {
    await mutateAsync();
  };

  return (
    <S.UserMenuButton onClick={handleLogoutButton}>
      <img src={userMenuIcon} alt='사용자 메뉴' />
    </S.UserMenuButton>
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
