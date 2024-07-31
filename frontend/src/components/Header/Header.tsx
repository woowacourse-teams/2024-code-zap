import { useState } from 'react';
import { Link } from 'react-router-dom';

import { logoIcon, newTemplateIcon } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import * as S from './style';

const Header = () => {
  const [searchValue, setSearchValue] = useState('');

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  return (
    <S.HeaderContainer>
      <Link to={'/'}>
        <Flex align='center' gap='1.2rem' width='fit-content'>
          <img src={logoIcon} alt='logo' />
          <Text.SubTitle color='#FFD269'>CodeZap</Text.SubTitle>
        </Flex>
      </Link>
      <Flex align='center' gap='3rem' flex='1'>
        <Link to={'/my-page'}>
          <Button size='medium' variant='text'>
            <Text.Body weight='bold' color='#FFD269'>
              MyPage
            </Text.Body>
          </Button>
        </Link>
        <Link to={'/'}>
          <Button size='medium' variant='text'>
            <Text.Body weight='bold' color='white'>
              Explores
            </Text.Body>
          </Button>
        </Link>
      </Flex>
      <Flex align='center' gap='3rem' width='fit-content'>
        <Input
          value={searchValue}
          onChange={handleInputChange}
          placeholder='Search...'
          type='search'
          width='40rem'
          height='4rem'
          fontSize='1.6rem'
        />
        <Link to={'/templates/upload'}>
          <Button size='medium' variant='outlined'>
            <img src={newTemplateIcon} alt='newTemplate' />
            <Text.Body weight='bold' color='#FFD269'>
              New Template
            </Text.Body>
          </Button>
        </Link>
      </Flex>
    </S.HeaderContainer>
  );
};

export default Header;
