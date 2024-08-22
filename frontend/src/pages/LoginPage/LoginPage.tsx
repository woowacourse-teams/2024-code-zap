import { Link } from 'react-router-dom';

import { EyeIcon } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import { useLoginForm } from '@/hooks/authentication/useLoginForm';
import { useToggle } from '@/hooks/utils';
import { END_POINTS } from '@/routes';
import * as S from './LoginPage.style';

const LoginPage = () => {
  const [showPassword, handlePasswordToggle] = useToggle();
  const { name, password, errors, handleNameChange, handlePasswordChange, isFormValid, handleSubmit } = useLoginForm();

  return (
    <>
      <S.ResponsiveFlex direction='column' justify='center' align='center' height='100vh'>
        <S.LoginPageContainer direction='column' justify='center' align='center' gap='3.5rem' width='27.5rem'>
          <Flex direction='column' justify='center' align='center' gap='1rem'>
            <S.ResponsiveHeading color='#F79037'>환영하잽</S.ResponsiveHeading>
          </Flex>

          <S.LoginForm
            onSubmit={handleSubmit}
            style={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', gap: '1rem' }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.name}>
              <Input.Label>아이디</Input.Label>
              <Input.TextField type='text' value={name} onChange={handleNameChange} autoComplete='username' />
              <Input.HelperText>{errors.name}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.password}>
              <Input.Label>비밀번호</Input.Label>
              <Input.TextField
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={handlePasswordChange}
                autoComplete='current-password'
              />
              <Input.Adornment>
                <EyeIcon onClick={handlePasswordToggle} css={{ cursor: 'pointer' }} aria-label='비밀번호 보기' />
              </Input.Adornment>
              <Input.HelperText>{errors.password}</Input.HelperText>
            </Input>

            <Button type='submit' variant='contained' fullWidth disabled={!isFormValid()}>
              로그인
            </Button>
            <Flex justify='flex-end' align='center' width='100%' gap='0.5rem'>
              <Text.XSmall color='#6B7079'>계정이 없으신가요?</Text.XSmall>

              <Link to={END_POINTS.SIGNUP}>
                <Button variant='text' size='small'>
                  회원가입
                </Button>
              </Link>
            </Flex>
          </S.LoginForm>
        </S.LoginPageContainer>
      </S.ResponsiveFlex>
    </>
  );
};

export default LoginPage;
