import { Link } from 'react-router-dom';

import { logoIcon, newTemplateIcon, userMenuIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { theme } from '../../style/theme';
import * as S from './Header.style';

const Header = () => (
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
        <UserMenuButton />
      </Flex>
    </S.HeaderContentContainer>
  </S.HeaderContainer>
);

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

const UserMenuButton = () => (
  <S.UserMenuButton>
    <img src={userMenuIcon} alt='사용자 메뉴' />
  </S.UserMenuButton>
);

export default Header;
