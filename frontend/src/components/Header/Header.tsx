import { Link } from 'react-router-dom';

import { logoIcon, newTemplateIcon, userMenuIcon } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
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
        <NewTemplateButton />
        <UserMenuButton />
      </Flex>
    </S.HeaderContentContainer>
  </S.HeaderContainer>
);

const Logo = () => (
  <Link to={'/'}>
    <Flex align='center' gap='1rem'>
      <img src={logoIcon} alt='로고 버튼' />
      <Heading.XSmall color='#FF9500'>코드잽</Heading.XSmall>
    </Flex>
  </Link>
);

const NavOption = ({ route, name }: { route: string; name: string }) => (
  <Link to={route}>
    <Button size='medium' variant='text'>
      <Text.Medium weight='bold' color='#393E46'>
        {name}
      </Text.Medium>
    </Button>
  </Link>
);

const NewTemplateButton = () => (
  <Link to={'/templates/upload'}>
    <S.NewTemplateButton>
      <img src={newTemplateIcon} alt='' />
      <Text.Small weight='bold' color='#FF9500'>
        새 템플릿
      </Text.Small>
    </S.NewTemplateButton>
  </Link>
);

const UserMenuButton = () => (
  <S.UserMenuButton>
    <img src={userMenuIcon} alt='사용자 메뉴' />
  </S.UserMenuButton>
);

export default Header;
