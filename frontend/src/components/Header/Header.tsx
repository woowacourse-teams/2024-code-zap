import { useState } from 'react';
import { Link } from 'react-router-dom';

import { logoIcon, newTemplateIcon, searchIcon } from '@/assets/images';
import { Button, Flex, Heading, Input, Text } from '@/components';
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
          <Heading.Large color='#FFD269'>CodeZap</Heading.Large>
        </Flex>
      </Link>
      <Flex align='center' gap='3rem' flex='1'>
        <Link to={'/my-page'}>
          <Button size='medium' variant='text'>
            <Text.Medium weight='bold' color='#FFD269'>
              MyPage
            </Text.Medium>
          </Button>
        </Link>
        <Link to={'/'}>
          <Button size='medium' variant='text'>
            <Text.Medium weight='bold' color='white'>
              Explores
            </Text.Medium>
          </Button>
        </Link>
      </Flex>
      <Flex align='center' gap='3rem' width='fit-content'>
        <Input size='medium' variant='outlined'>
          <Input.Adornment>
            <img src={searchIcon} />
          </Input.Adornment>
          <Input.TextField placeholder='Search...' onChange={handleInputChange} type='search' value={searchValue} />
        </Input>
        <Link to={'/templates/upload'}>
          <Button size='medium' variant='outlined'>
            <img src={newTemplateIcon} alt='newTemplate' />
            <Text.Medium weight='bold' color='#FFD269'>
              New Template
            </Text.Medium>
          </Button>
        </Link>
      </Flex>
    </S.HeaderContainer>
  );
};

export default Header;
