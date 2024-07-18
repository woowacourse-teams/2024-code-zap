import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import logoIcon from '../../assets/images/logo.png';
import newTemplateIcon from '../../assets/images/newTemplate.png';
import { Button } from '../Button';
import { Flex } from '../Flex';
import { Input } from '../Input';
import { Text } from '../Text';
import { HeaderContainer } from './style';

const Header = () => {
  const [searchValue, setSearchValue] = useState('');

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  return (
    <HeaderContainer>
      <Link to={'/my-page'}>
        <Flex align='center' gap='1.2rem' width='fit-content'>
          <img src={logoIcon} alt='logo' />
          <Text.SubTitle weight='bold' color='#FFD269'>
            CodeZap
          </Text.SubTitle>
        </Flex>
      </Link>
      <Flex align='center' gap='3rem' flex='1'>
        <Link to={'/my-page'}>
          <Button type='text'>
            <Text.Body weight='bold' color='#FFD269'>
              MyPage
            </Text.Body>
          </Button>
        </Link>
        <Link to={'/'}>
          <Button type='text'>
            <Text.Body weight='bold'>Explores</Text.Body>
          </Button>
        </Link>
      </Flex>
      <Flex align='center' gap='3rem' width='fit-content'>
        <Input
          value={searchValue}
          onChange={handleInputChange}
          placeholder='Search...'
          type='search'
          max-width='40rem'
          height='4rem'
          fontSize='1.6rem'
        />
        <Link to={'/templates/uploads'}>
          <Button type='outlined' width='fit-content'>
            <img src={newTemplateIcon} alt='newTemplate' />
            <Text.Body weight='bold' color='#FFD269'>
              New Template
            </Text.Body>
          </Button>
        </Link>
      </Flex>
    </HeaderContainer>
  );
};

export default Header;
