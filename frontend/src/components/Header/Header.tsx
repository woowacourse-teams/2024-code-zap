import React, { useState } from 'react';
import { Flex } from '../Flex';
import { Text } from '../Text';
import { Input } from '../Input';
import { Button } from '../Button';
import { HeaderContainer } from './style';
import logoIcon from '../../assets/images/logo.png';
import newTemplateIcon from '../../assets/images/newTemplate.png';

const Header = () => {
  const [searchValue, setSearchValue] = useState('');

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  return (
    <HeaderContainer>
      <Flex align='center' gap='1.2rem' width='fit-content'>
        <img src={logoIcon} alt='logo' />
        <Text.SubTitle weight='bold' color='#FFD269'>
          CodeZap
        </Text.SubTitle>
      </Flex>
      <Flex align='center' gap='3rem' flex='1'>
        <Button onClick={() => {}} type='text'>
          <Text.Body weight='bold' color='#FFD269'>
            MyPage
          </Text.Body>
        </Button>
        <Button onClick={() => {}} type='text'>
          <Text.Body weight='bold'>Explores</Text.Body>
        </Button>
      </Flex>
      <Flex align='center' gap='3rem' width='fit-content'>
        <Input
          value={searchValue}
          onChange={handleInputChange}
          placeholder='Search...'
          type='search'
          width='45rem'
          fontSize='1.6rem'
        />
        <Button onClick={() => {}} type='outlined' width='fit-content'>
          <img src={newTemplateIcon} alt='newTemplate' />
          <Text.Body weight='bold' color='#FFD269'>
            New Template
          </Text.Body>
        </Button>
      </Flex>
    </HeaderContainer>
  );
};

export default Header;
